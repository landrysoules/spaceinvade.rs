---
layout: post.html
title: JHipster tips & tricks
tags: [Java, JHipster]
---

* Comment changer le context de l'application (une seule var à ajouter dans le fichier de conf de l'appli, rien à faire côté angular)

* Pour liquibase, penser à changer les infos de connexion dans pom.xml si besoin, sinon les updates sur la base ne fonctionneront pas.

* If you choose to go the SASS/COMPASS way, make sure you have compass installed (gem install compass, or sudo gem install compass if you're not using RVM).
Par ailleurs, tu devras lancer grunt serve (pour servir l'appli angular, avec proxy automatique sur tomcat pour la partie server), ou grunt wiredep pour traiter les js, css etc.
Penser à le faire après chaque changement.

* JHipster a fait le choix de ne pas utiliser angular-ui, mais du vanilla bootstrap/jquery et pour ce que j'en vois c'est un excellent choix !
On se prend vachement moins la tête à utiliser des trucs tout bêtes comme un modal ou un datepicker !
==> à nuancer: le datepicker est un cauchemar dès que tu veux simplement formatter une date, obligé de repasser à angularUI. Par contre pour l'instant en tout cas le modal sauce hipster semble plutôt pas mal.

Pour lancer les test: karma semble moins pete burnes et plus rapide que protractor.

Pour installer une dépendance, utiliser bower install ma-dependance --save  => ça va l'écrire où il faut dans index.html.

Pour cucumber, voir la task cuke que j'ai ajouté dans Gruntfile. 
Il suffit donc de lancer grunt cuke et c'est parti.

*Attention* Pour que ça fonctionne, il faut au préalable que l'app tourne sur le serveur (lancer l'appli côté serveur sur eclipse + grunt serve), et que webdriver tourne (webdriver-manager start)
**Attention 2** Il faut avant tout ça avoir installé cucumber.js, et l'avoir configuré dans protractor (voir ma config). J'ai tellement essayé de faire marcher cucumber en vain de x façons différentes que je ne sais plus trop ce qu'il faut installer, mais ça ne devrait pas être trop difficile de réisntaller la partie cucumber from scratch.

Ensuite, il faut utiliser l'api de selenium/webdriver pour naviguer et chai pour les assertions.


Attention, que ce soit pour karma ou protractor, ne jamais minimiser la fenetre chrome de test, sinon ça prend 3 plombes.


Penser impérativement à appeler callback() à la fin de chaque step définition, sinon tu chopes une erreur : "[launcher] BUG: launcher exited with 1 tasks remaining" => il s'avere que ce n'est pas si simple ! Si tu ajoutes callback() à la fin de chaque step, il passe n'importe quel step en success, même s'il est délirant

Services/factories

Attention: la manière de les déclarer est hyper importante de manière à pouvoir ensuite appeler des functions à l'intérieur du service (sinon il te dira que this n'est pas défini)

Service:

'use strict';

angular.module('sdopApp')
    .service('Map', [function() {
        var self = this;
           this.buildMarkers = function (locations) {
                var myLocationsMarkers = {};
                locations.forEach(function(location){
                    myLocationsMarkers[location.locationId] = self.buildMarker(location);
                });
                return myLocationsMarkers;
            };
            this.buildMarker = function(location){
                var marker = {
                    lat: location.boxCoordinates.latSW,
                    lng: location.boxCoordinates.lngSW,
                    label: {
                        message: location.locationId,
                        options: {
                           noHide: true
                        }
                    }
                };
                return marker;
            };
    } ]);

Factory:   **Penser à retourner this!**

'use strict';

angular.module('sdopApp')
    .factory('Map', [function() {
        var self = this;
           this.buildMarkers = function (locations) {
                var myLocationsMarkers = {};
                locations.forEach(function(location){
                    myLocationsMarkers[location.locationId] = self.buildMarker(location);
                });
                return myLocationsMarkers;
            };
            this.buildMarker = function(location){
                var marker = {
                    lat: location.boxCoordinates.latSW,
                    lng: location.boxCoordinates.lngSW,
                    label: {
                        message: location.locationId,
                        options: {
                           noHide: true
                        }
                    }
                };
                return marker;
            };
            return this;
    } ]);


    ----------------------

Avec ngMessages, on ne peut utiliser la directive translate: il faut impérativement utiliser le filter

Pour spring security on a intéret à modifier le mécanisme de base qui permet de récupérer uniquement le login user, ce qui est léger :
Create Extention class:


public class CustomUserDetails extends org.springframework.security.core.userdetails.User{

    private User user;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), user.getPassword(), authorities);
        this.user = user;
    }

    public CustomUserDetails(User user, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
Than add it to UserDetailsService:

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
    UserDetails userDetails = null;
    User user = userService.getByLogin(login);
    userDetails = new CustomUserDetails(user,
                true, true, true, true,
                getAuthorities(user.getRole()));

    return userDetails;
}
Get it!

 (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()   

 ----------------

Créer un BaseBean, dont héritent tous les autres, ça permet notamment de mettre les champs id, updated et cie au même endroit (et ne pas oublier le persislistener qui v automatiquement mettre ces champs à jour)

---------------

Gestion centralisée des erreurs http avec envoi de messages par toaster
=> ne pas oublier de gérer waitProcess pour chaque action en cas d'erreur
C'est d'ailleurs tout ce que tu as à faire en cas d'erreur, l'intercepteur prenant tout en charge.
