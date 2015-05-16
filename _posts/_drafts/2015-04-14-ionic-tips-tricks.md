---
layout: post.html
title: Ionic framework tips & tricks
tags: [ionic]
---

Comment ajouter minification 
installer [cordova-uglify](https://github.com/rossmartin/cordova-uglify)
C’est lancé ensuite automatiquement à chaque build, y compris ionic (run android par exemple)

Comment ajouter icone de l’app
Renvoyer vers http://learn.ionicframework.com/formulas/adding-an-icon/, en expliquant comment faire pour android

Comment résoudre ClassNotFoundException avec le nom de ton app lors du déploiement via play, alors que ça marche impec avec ionic run android
Symptomes: tu installes depuis market, et l’app crash au lancement, expliquer comment repérer l’erreur en lancant ddms et launch de l’appli, tel connecté au pc
Solution: Créer le package et la classe correspondant à ton appli, et au package déclarés dans config.xml
