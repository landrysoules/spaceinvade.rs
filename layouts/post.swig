{% extends 'base.swig' %}


{% block breadcrumbs %}
    <ul id="breadcrumbs">
        <li><a href="{{ get_url() }}">Blog</a><span>&rsaquo;</span></li>
        <li>{{ title }}</li>
    </ul>
{% endblock %}

{% block content %}
<div class="post_content">
<div class="row item">
   <!-- <div class="item">-->
        <div class="item-header">
            <h1>{{ title }}</h1>
          <h2>{{ item.date }}{% if tags %} <span>&raquo;</span> {% for tag in tags %}<a href="#">{{ tag }}</a>{% if not loop.last %}, {% endif %}{% endfor %}{% endif %}</h2>
        </div>

     </div>

        <div class="row item-body">
            {{ contents | safe }}
        </div>

        <div class="row item-body">
            <g:plusone></g:plusone>			<a href="https://twitter.com/share" class="twitter-share-button" data-via="opensource_dev">Tweet</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
<script src="//platform.linkedin.com/in.js" type="text/javascript">
  lang: en_US
</script>
<script type="IN/Share" data-counter="right"></script>
        </div>


    <div id="disqus_thread" class="item-body"></div>
<script type="text/javascript">
    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
    var disqus_shortname = 'spaceinvaders'; // Required - Replace example with your forum shortname

    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function() {
        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
        dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
    })();
</script>
<noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
<a href="http://disqus.com" class="dsq-brlink">blog comments powered by <span class="logo-disqus">Disqus</span></a>




 <script type="text/javascript">
  (function() {
    var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
    po.src = 'https://apis.google.com/js/plusone.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
  })();
</script>


<!--</div>-->
</div>
{% endblock %}
