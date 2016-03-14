package rs.prozone.acam.pi.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import rs.prozone.acam.pi.entity.BaseBean;
import rs.prozone.acam.pi.entity.BaseVersionBean;
import rs.prozone.acam.pi.entity.search.Pager;
import rs.prozone.acam.pi.entity.search.SearchResult;
import rs.prozone.acam.pi.repository.EntityRepository;
import rs.prozone.acam.pi.service.AcasService;


@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public abstract class AcasServiceImpl<T extends BaseBean, ID extends Serializable> implements AcasService<T, ID> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	protected EntityManager em;

	protected Class<T> entityClass;

	@Autowired
	protected CrudRepository<T, ID> repository;

	@Autowired
	private EntityRepository<T> entityRepository;

	@SuppressWarnings("unchecked")
	public AcasServiceImpl() {

		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public T save(T entity) throws Exception {
		// logika snimanja atributa izmenjen, kreiran i sl. su u
		// acas.pi.listener.PersistListener

		/*
		 * Korisnik authenticatedKorisnik =
		 * SecurityUtils.getCurrentAuthenticatedUser(); Long id =
		 * entity.getId(); Date now = new Date(); if (id != null) { T
		 * alreadySavedEntity = repository.findOne((ID) id); if
		 * (alreadySavedEntity == null) throw new
		 * EntityNotFoundException("No object found with the id #" + id);
		 * entity.setKreiran(alreadySavedEntity.getKreiran());
		 * entity.setKreirano(alreadySavedEntity.getKreirano()); }
		 * 
		 * entity.setIzmenjeno(now);
		 * entity.setIzmenjen(authenticatedKorisnik.getId()); if
		 * (entity.getKreirano() == null) { entity.setKreirano(now);
		 * entity.setKreiran(authenticatedKorisnik.getId()); }
		 */
		return repository.save(entity);
	}

	@Override
	public T get(ID id) throws Exception {
		return repository.findOne(id);
	}

	@Override
	public List<T> getAll() throws Exception {
		List<T> entities = (List<T>) repository.findAll();
		if (entities == null)
			entities = new ArrayList<T>();
		return entities;
	}

	@Override
	public void delete(ID id) throws Exception {
		T entity = repository.findOne(id);
		if (entity != null) {
			if (entity instanceof BaseVersionBean) {
				((BaseVersionBean) entity).setObrisano(new Date());
				((BaseVersionBean) entity).setObrisan(1L);
				repository.save(entity);
			} else {
				repository.delete(entity);
			}
		} else {
			throw new EntityNotFoundException("No object found with the id #" + id);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(Iterable<T> entities) throws Exception {
		for (T entity : entities) {
			delete((ID) entity.getId());
		}
	}

	public boolean canBeDeleted(Long id) {
		return entityRepository.canBeDeleted(entityClass, id);
	}

	@Override
	public void physicalDelete(ID id) throws Exception {
		repository.delete(id);
	}

	@Override
	public Iterable<T> saveAll(List<T> entities) throws Exception {
		return repository.save(entities);
	}

	/**
	 * Gets all non-static and non-transient class fields
	 * 
	 * @author djordje.mijailovic
	 *
	 */
	private static class FieldCbk implements FieldCallback {
		private List<Field> fields = new ArrayList<Field>();

		@Override
		public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
			// Ne interesuju nas staticna polja
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
				fields.add(field);
			}
		}

		public List<Field> getFields() {
			return fields;
		}

		public static List<Field> getEntityFields(Class<?> classs) {
			FieldCbk fcbk = new FieldCbk();
			ReflectionUtils.doWithFields(classs, fcbk);
			return fcbk.getFields();
		}
	}

	/**
	 * Generise predikate na osnovu prosledjenog SearchForm objekta. Rekurzivno
	 * prolazi kroz sve ugnjezdene SearchForm objekte i generise predikate i za
	 * njih. Moguce je promeniti ponasanje generisanja predikata za odredjena
	 * polja. Da bi se to postiglo treba u konkretnoj implementaciji servisa
	 * promeniti neke od handleXxxXxx() metoda. Napomena: promena ponasanja je
	 * moguca trenutno samo za polja root search forme. Ne primenjuje se nad
	 * poljima ugnjezdenih formi. Polja za koja ne zelimo da se generise
	 * predikat automatski oznacavamo modifier-om "transient"
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SearchResult<T> search(final T searchForm, Pager pager, Set<String> ignoreFields) throws Exception {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		final Root<T> root = criteriaQuery.from(entityClass);

		// To solve the problem when no size is transmitted from the client
		// application, for example on first load.
		if (pager != null) {
			if (pager.getSize() == null)
				pager.setSize(10000);
		}

		// List of all predicates taht will be applied in "where" part
		// ========================================
		ArrayList<Predicate> predicates = new ArrayList<Predicate>();

		// Process all of fields in current class
		// and generates predicates for all of them
		// Generated predicates are added to "predicates" list
		// ========================================
		generatePredicates(predicates, criteriaBuilder, root, searchForm);

		if (searchForm instanceof BaseVersionBean) {
			// Added to return only records not logically deleted.
			predicates.add(criteriaBuilder.isNull(root.get("obrisano")));
		}

		// Execute query
		// ========================================

		// From
		criteriaQuery.select(root);

		// Where
		Predicate[] predArray = new Predicate[predicates.size()];
		for (int i = 0; i < predArray.length; i++) {
			predArray[i] = predicates.get(i);
		}
		criteriaBuilder.and(predArray);
		criteriaQuery.where(criteriaBuilder.and(predArray));

		// Order by
		List<Order> orders = generateOrderBy(pager, root);
		if (orders != null && !orders.isEmpty()) {
			criteriaQuery.orderBy(orders);
		}

		Long totalCount = null;
		Boolean isFirst = false;
		Boolean isLast = false;
		int index = 1;
		int totalPages = 0;

		// Glavni upit
		Query qry = em.createQuery(criteriaQuery);

		// Pagination. It is done only if paging parameters are present
		if (pager != null && pager.getSize() != null && pager.getPage() != null) {

			// Limit data in main query
			qry.setFirstResult(pager.getSize() * (pager.getPage() - 1));
			qry.setMaxResults(pager.getSize());

			CriteriaBuilder qb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = qb.createQuery(Long.class);
			cq.select(qb.count(cq.from(entityClass)));
			cq.where(criteriaBuilder.and(predArray));
			if (orders != null && !orders.isEmpty()) {
				cq.orderBy(orders);
			}
			totalCount = em.createQuery(cq).getSingleResult();
			isFirst = pager.getPage() == 1;
			isLast = Math.ceil(Double.valueOf(totalCount) / pager.getSize()) == pager.getPage();
			if ((totalCount % pager.getSize()) == 0) {
				totalPages = Math.round(totalCount / pager.getSize());
			} else {
				totalPages = ((int) Math.floor(totalCount / pager.getSize())) + 1;
			}
			int page = pager.getPage();
			if (page > 1) {
				index = ((page - 1) * pager.getSize()) + 1;
			}
		}

		// Execute query and get all data
		SearchResult<T> result = new SearchResult<T>(qry.getResultList(), totalCount);
		result.setPager(pager);
		result.setFirst(isFirst);
		result.setLast(isLast);
		result.setIndex(index);
		result.setTotalPages(totalPages);
		return result;
	}

	protected List<Order> generateOrderBy(Pager pager, Root<T> root) {
		List<Order> orders = new ArrayList<Order>();
		if (pager != null) {
			String orderBys = pager.getSort();
			if (orderBys != null) {
				// Sort po vise kolona
				String[] orderByparts = orderBys.split(";");
				for (String orderBy : orderByparts) {
					// Pojedinacni sortovi
					String[] orderParts = orderBy.split(":");
					String colName = orderParts[0];
					String direction = orderParts[1];
					boolean isAscending = true;
					if (orderParts.length == 2) {
						if (direction.equalsIgnoreCase(Pager.DIR_DESC)) {
							isAscending = false;
						}
					}
					OrderImpl o = new OrderImpl(parsePath(colName, root), isAscending);
					orders.add(o);
				}
			}
		}
		return orders;
	}

	private Path<?> parsePath(String path, Path<?> root) {
		Path<?> retPath = root;
		String[] pathElements = path.split("\\.");
		for (String pathElement : pathElements) {
			retPath = retPath.get(pathElement);
		}
		return retPath;
	}

	/**
	 * Prolazi kroz sva polja i obradjuje ih. Rekurzivno prolazi kroz ostale
	 * search forme u proslednjenom entitetu
	 * 
	 * @param criteriaBuilder
	 * @param path
	 * @param field
	 */
	protected void generatePredicates(ArrayList<Predicate> predicates, CriteriaBuilder criteriaBuilder, Path<?> path, BaseBean entity) {

		// Get all fields from class
		// ========================================
		List<Field> fields = FieldCbk.getEntityFields(entity.getClass());
		// FIXME: izbaci ignorisana polja za root

		for (Field field : fields) {

			ReflectionUtils.makeAccessible(field);
			Object value = ReflectionUtils.getField(field, entity);

			if (value != null) {
				Predicate predicate = null;

				if (value instanceof String && !StringUtils.isEmpty((String) value)) {
					predicate = handleStringField(field, criteriaBuilder, path, (String) value);

				} else if (value instanceof Boolean) {
					predicate = handleBoolean(field, criteriaBuilder, path, (Boolean) value);

				} else if (value instanceof Long || value instanceof Integer) {
					predicate = handleLongAndIntField(field, criteriaBuilder, path, Long.valueOf(value.toString()));

				} else if (value instanceof Date) {
					predicate = handleDateField(field, criteriaBuilder, path, (Date) value);

				} else if (value instanceof Collection) {
					predicate = handleCollection(field, criteriaBuilder, path, (Collection<?>) value);

				} else if (value instanceof BaseBean) {
					// u slucaju da je u pitanju bean, poredimo jednakost. U
					// slucaju da je search forma povezanog bean-a
					// rekurzivno dodajemo predikate za njena polja
					// TODO: Pronaci pametniji nacin da se utvrdi da li je u
					// pitanju bean ili forma
					if (value.getClass().getSuperclass().equals(BaseBean.class) || value.getClass().getSuperclass().equals(BaseVersionBean.class)) {
						predicate = handleEntityField(field, criteriaBuilder, path, (BaseBean) value);
					} else {
						handleSearchFormField(predicates, field, criteriaBuilder, path, (BaseBean) value);
					}
				} else {
					// TODO: baci izuzetak?
				}

				if (predicate != null) {
					predicates.add(predicate);
				}
			}
		}

	}

	/**
	 * 
	 * @param field
	 * @param criteriaBuilder
	 * @param path
	 * @param value
	 * @return
	 */
	protected Predicate handleStringField(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, String value) {
		String fieldName = field.getName();

		Predicate stringEqualsPr = null;
		if (fieldName.endsWith("_like")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_like"));
			stringEqualsPr = criteriaBuilder.like(path.<String> get(fieldName), value);
		} else if (fieldName.endsWith("_like_r")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_like_r"));
			stringEqualsPr = criteriaBuilder.like(path.<String> get(fieldName), value + "%");
		} else {
			stringEqualsPr = criteriaBuilder.equal(path.get(fieldName), value);
		}

		return stringEqualsPr;
	}

	protected Predicate handleLongAndIntField(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, Long value) {
		String fieldName = field.getName();

		Predicate predicate = null;
		if (fieldName.endsWith("_od")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_od"));
			predicate = criteriaBuilder.greaterThanOrEqualTo(path.<Long> get(fieldName), value);
		} else if (fieldName.endsWith("_do")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_do"));
			predicate = criteriaBuilder.lessThanOrEqualTo(path.<Long> get(fieldName), value);
		} else {
			predicate = criteriaBuilder.equal(path.get(fieldName), value);
		}

		return predicate;
	}

	protected Predicate handleDateField(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, Date value) {
		String fieldName = field.getName();

		Predicate predicate = null;
		if (fieldName.endsWith("_od")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_od"));
			predicate = criteriaBuilder.greaterThanOrEqualTo(path.<Date> get(fieldName), value);
		} else if (fieldName.endsWith("_do")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_do"));
			predicate = criteriaBuilder.lessThanOrEqualTo(path.<Date> get(fieldName), value);
		} else {
			predicate = criteriaBuilder.equal(path.get(fieldName), value);
		}
		return predicate;
	}

	protected Predicate handleCollection(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, Collection<?> value) {
		Predicate predicate = null;
		String fieldName = field.getName();
		if (fieldName.endsWith("_in")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_in"));
			predicate = criteriaBuilder.and(path.get(fieldName).in(value));
		}
		return predicate;
	}

	private Predicate handleBoolean(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, Boolean value) {
		Predicate predicate = null;
		String fieldName = field.getName();

		if (fieldName.endsWith("_is_null")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_is_null"));
			if (Boolean.TRUE.equals(value))
				predicate = criteriaBuilder.isNull(path.get(fieldName));
			else
				predicate = criteriaBuilder.isNotNull(path.get(fieldName));
		} else {
			predicate = criteriaBuilder.equal(path.get(fieldName), value);
		}

		return predicate;
	}

	protected Predicate handleEntityField(Field field, CriteriaBuilder criteriaBuilder, Path<?> path, BaseBean value) {
		String fieldName = field.getName();

		return criteriaBuilder.equal(path.get(fieldName), value);
	}

	protected void handleSearchFormField(ArrayList<Predicate> predicates, Field field, CriteriaBuilder criteriaBuilder, Path<?> path, BaseBean value) {

		String fieldName = field.getName();
		if (fieldName.endsWith("_s")) {
			fieldName = fieldName.substring(0, fieldName.indexOf("_s"));
			generatePredicates(predicates, criteriaBuilder, path.get(fieldName), value);
		}
	}
}
