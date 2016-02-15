<!DOCTYPE html>

<html>
  <head>
    <title>{% if title %}{{ title }}
      –
      {% endif %}{{ site.title }}{% if not
    title and site.subtitle %}
      –
      {{ site.subtitle }}{% endif %}</title>
    {% include "header.swig" %}
  </head>

  <body>
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand banner" href="#">{{site.title}}</a>
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <ul class="nav nav-pills pull-right">
          <li role="presentation" class="active">
            <a href="/">Home</a>
          </li>
          <li role="presentation">
            <a href="/pages/articles">Articles</a>
          </li>
          <li role="presentation">
            <a href="#">Messages</a>
          </li>
        </ul>
      </div>
    </nav>

    {% if site.title %}{% endif %}

    <div class="container">
<div class="row">
      <div class="col-xs-12 col-md-8" id="content">
        <div class="row item">
          {% block breadcrumbs %}{% endblock %}
        </div>{% block content %}{% endblock %}
      </div>
</div>
    </div>
  </body>
</html>
