{% extends 'base.swig' %}

{% block content %}
<div class="post_content">
  <div class="row item">
    <div class="item-header">
      <h1>{{ site.sub }}</h1>
      <h2>{{ item.date }}{% if tags %}
        <span>&raquo;</span>
        {% for tag in tags %}
        <a href="#">{{ tag }}</a>{% if not loop.last %},
        {% endif %}{% endfor %}{% endif %}</h2>
    </div>

  </div>

  <div class="row item-body">
    {{ contents | safe }}
  </div>

</div>
{% endblock %}
