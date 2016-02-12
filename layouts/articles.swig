{% extends 'base.swig' %}

{% set title = 'Blog &rsaquo; Archives  &rsaquo; ' ~ archive.year %}

{% block content %}
<div class="row archive">
  <div class="horizontal archives">
    {% for post in collections.posts %}
    <div class="row">
      <div class="12-small 12-large 12-medium">
        <span class="archives-post-date">{{ post.timestamp|date('%b %d') }}</span>
        <span class="archives-post-title">
          <a href="/{{post.path }}">{{ post.title }}</a>
        </span>
      </div>
    </div>
    {% endfor %}
  </div>
</div>
{% endblock %}
