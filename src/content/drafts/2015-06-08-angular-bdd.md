---
layout: post.html
title: AngularJS the BDD way
tags: [angular, BDD]
---

Les outils: 
Pour les tests unitaires: karma
Pour les tests e2e: protractor
Jasmine est l'équivalent de rspec.

Installation:
# Install Karma:
$ npm install karma --save-dev

# Install plugins that your project needs:
$ npm install karma-jasmine karma-chrome-launcher --save-dev

$ npm install protractor --save-dev

Typing ./node_modules/karma/bin/karma start sucks and so you might find it useful to install karma-cli globally.
$ npm install -g karma-cli

Les tests unitaires:
Jhipster les a générés, dans src/test/javascript, ainsi que le fichier de config de karma.
karma start src/test/javascript/karma.conf.js
Pour les lancer,
karma start src/test/javascript/karma.conf.js
Si j'ai bien compris, ça lance le serveur karma, et chaque fois que tu fais un changement sur un test, ça les relance.
Une fois le serveur démarré, tu peux aussi lancer des tests manuellement, en executant dans une autre fenetre 
karma run src/test/javascript/karma.conf.js
e
Pour cucumber:
npm install cucumber --save-dev
npm install grunt-cucumber --save-dev

Créer un dossier src/test/javascript/features dans lequel on mettra les features et un dossier src/test/javascript/features/steps pour les steps
Ajouter dans Gruntfile.js:
grunt.initConfig({
  cucumberjs: {
    src: 'path/to/features',
    options: {
      steps: "path/to/step_definitions"
    }
  }
});

et

grunt.loadNpmTasks('grunt-cucumber');

=====================================================

Bonne méthode pour cucumber:

Trouvé de l'aide ici: http://angular.github.io/protractor/#/frameworks

et ici: http://christian.fei.ninja/Write-your-Protractor-tests-in-Cucumber/

Installer protractor de manière globale (pour avoir aussi accès a son CLI): 
npm install -g protractor

Puis
webdriver-manager update
(afin d'updater selenium et d'autres plugins utilisés par protractor)

Installer également cucumber de manière globale : npm install -g cucumber.js
(je suis certain de devoir l'installer globalement, sinon on obtient une erreur lors de l'exécution, mais je ne sais pas si l'installation au niveau de l'appli, avec npm install cucumber --save-dev est également necessaire => à tester)

Lancer ensuite le server selenium: il doit tourner pendant toute la durée des tests:
webdriver-manager start

Installer chai, une lib d'assertions: npm install chai --save-dev

Créer à la racine du projet un fichier protractor-cuke.conf.js (on peut le nommer comme on veut):

exports.config = {
  directConnect: true,

  // Capabilities to be passed to the webdriver instance.
  capabilities: {
    'browserName': 'chrome'
  },

  // Framework to use. Jasmine 2 is recommended.
  framework: 'cucumber',
      specs: ['**/features/authentication.feature'],
  cucumberOpts: {
    require: 'src/test/javascript/steps/auth_steps.js',
    format: 'progress'
  },

  // Spec patterns are relative to the current working directly when
  // protractor is called.
  //specs: ['example_spec.js'],

  // Options to be passed to Jasmine.
  jasmineNodeOpts: {
    defaultTimeoutInterval: 30000
  }
};

https://github.com/angular/protractor/blob/master/docs/frameworks.md

Je me retrouve avec un 
[launcher] BUG: launcher exited with 1 tasks remaining
Je suppose que c'est du au fait qu'angular soit async, et donc problème avec chaijs. Chaijsaspromise est censé régler le problème => voir ce qui merde.

-----------------

Tous les problèmes cucumber semblent désormais réglés, voir dans sdop comment ça fonctionne. On peut lancer les features avec grunt cuke

On se retrouve parfois avec une erreur du genre expect is undefined. Ca arrive lorsque tu appelles this.expect à l'intérieur d'un autre contexte (par exemple browser.get(...).then()).
Pour que ça marche il faut appeler this.expect à l'intérieur d'un given, when ou then, et pas à l'intérieur d'un autre contexte imbriqué.

Par ailleurs pour utiliser chai avec une promise protractor (genre isVisible()), il faut :

    4     this.Given(/^I am authenticated with admin role$/, function (callback) {
>>  5         browser.get('http://localhost:3000/#/login').then(function(){
>>  6             browser.findElement(By.id('username')).sendKeys('admin');
>>  7             var passwordField = browser.findElement(By.id('password'));
    8             passwordField.sendKeys('admin');
    9             passwordField.submit();
   10         });
   11         //this.expect(element(by.cssContainingText('.alert.alert-danger.ng-scope', 'Authentication failed'))).to.not.exist;
   12         //var condition = protractor.ExpectedConditions.textToBePresentInElement($('.alert.alert-danger.ng-scope'), 'Authentication failed');
>> 13         var elm = element(by.cssContainingText('.alert.alert-danger.ng-scope', 'Authentication failed'));
   14         this.expect(elm.isDisplayed()).to.eventually.to.be.false.and.notify(callback);
   15     });

   **eventually declenche l'utilisation de chai aspromised, tandis que and.notify résout la promise**

----------------------------
21.07.2015
    Au final j'ai passé la journée à essayer de passer des specs avec cucumber et c'est un bordel pas possible, ne serait-ce que pour tester si un tag est présent... Je retourne donc 
    à la méthode protractor, en attendant d'avoir vraiment bcp de temps pour faire marcher cucumber...

   Pour avoir une sortie détaillée des specs, installer ça [https://www.npmjs.com/package/jasmine-spec-reporter] et suivre [https://github.com/bcaudan/jasmine-spec-reporter/blob/master/docs/protractor-configuration.md]
