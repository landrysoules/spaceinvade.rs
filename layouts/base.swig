<!DOCTYPE html>
<html lang="en">

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>{% if title %}{{ title }} – {% endif %}{{ site.title }}{% if not title and site.subtitle %} – {{ site.subtitle }}{% endif %}</title>
  {% include "header.swig" %}
</head>

<body>
  <div class="container-fluid">
    <div class="row row-home">
      <div class="hidden-xs col-md-4 left-content text-center">
        <div>
          <a href="/" class="banner">spaceinvade.rs</a>
        </div>
        <div>
          <img src="/images/profile2.jpg" alt="Landry Soules" class="img-circle" height="200px" width="200px">
        </div>
        <p class="portrait">
          <div class="portrait">Landry Soules</div>
          <div class="portrait">full-stack developer</div>
        </p>
        <div>
          <ul class="list-unstyled list-inline">
            <li>
              <a href="https://github.com/landrysoules">
                    <i class="fa fa-github-alt fa-lg"></i>
                  </a>
            </li>
            <li>
              <a href="https://twitter.com/landry_soules">
                    <i class="fa fa-twitter fa-lg"></i>
                  </a>
            </li>
            <li>
              <a href="https://plus.google.com/u/0/+LandrySoules">
                    <i class="fa fa-google-plus fa-lg"></i>
                  </a>
            </li>
            <li>
              <a href="/rss.xml">
                    <i class="fa fa-rss fa-lg"></i>
                  </a>
            </li>
          </ul>
        </div>
        <div class="footer">
          <i class="fa fa-copyright fa-lg"></i> (MIT licence) Landry Soules 2014 - {{ now|date('Y') }}
          <p>POWERED BY <a href="http://metalsmith.io">METALSMITH</a></p>
        </div>
      </div>
      <div class="col-xs-12 col-md-8 main-content">
        {% block content %}{% endblock %}
      </div>
    </div>
  </div>
</body>

</html>
