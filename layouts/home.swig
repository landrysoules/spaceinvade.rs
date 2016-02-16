{% extends 'base.swig' %}

{% block content %}
{% for post in collections.posts %}
<article class="posts">
  <div class="posts-date">
    {{post.date|date('F jS, Y')}}
  </div>
  <div>
    <a href="/{{post.path }}" class="posts-title">{{ post.title }}</a>
  </div>
  <div class="posts-summary">
  {{ post.excerpt|safe }}
  </div>
</article>

{% endfor %}
{# </div> #}
{# </div> #}
{% endblock %}
