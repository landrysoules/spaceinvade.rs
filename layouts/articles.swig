{% extends 'base.swig' %}

{% set title = 'Blog &rsaquo; Archives  &rsaquo; ' ~ archive.year %}

{% block content %}
{# <div class="row archive"> #}
{# <div class="horizontal archives"> #}
{% for post in collections.posts %}
<div class="row">
  <div class="col-md-12">
    <span class="date">
      <time datetime="{{post.date|date('Y-m-d')}}" class="icon">
        <em>{{post.date|date('l')}}</em>
        <strong>{{post.date|date('F')}}</strong>
        <span>{{post.date|date('d')}}</span>
      </time>

      <span>{{post.date|date('Y-m-d')}}</span>
      {# <span class="archives-post-date">{{ post.timestamp|date('%b %d') }}</span> #}
      <span class="archives-post-title">
        <a href="/{{post.path }}">{{ post.title }}</a>
      </span>
    </div>
  </div>
  {% endfor %}
  {# </div> #}
  {# </div> #}
  {% endblock %}
