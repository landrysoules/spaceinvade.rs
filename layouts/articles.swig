{% extends 'base.swig' %}

{% set title = 'Blog &rsaquo; Archives  &rsaquo; ' ~ archive.year %}


{% block content %}
<div class="row archive">
    <div class="horizontal archives">
        {% for month in archive.months %}
            {% for post in month %}
           <div class="row">
           <div class="12-small 12-large 12-medium">
                <span class="archives-post-date">{{ post.timestamp|date('%b %d') }}</span>
                <span class="archives-post-title"><a href="{{ get_url(post.url) }}">{{ post.title }}</a></span>

                {% if post.tags %}
                    <span class="archives-post-tags">Tagged <span>&raquo;</span> {% for tag in post.tags %}<a href="{{ get_url(posts.tags[tag].url) }}">{{ tag }}</a>{% if not loop.last %}, {% endif %}{% endfor %}</span>
                {% endif %}
                </div>
           </div>
            {% endfor %}
        {% endfor %}
    </div>
</div>
{% endblock %}
