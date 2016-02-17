{% extends 'base.swig' %}

{% block content %}
<div class="row">
  <div class="col-md-12">
    <div class="post_content col-centered">
      <div class="row item">
        <div class="item-header">
          <h1 class="post.title">{{ title }}</h1>
          <div>{{date|date('F jS, Y')}}</div>
          <div>
          <ul class="list-unstyled list-inline">
        {% for tag in tags %}
          <li>
            <i class="fa fa-tag fa-lg"></i> <span>{{tag}}</span>
          </li>
        {% endfor %}
          </ul>
          </div>

        </div>

      </div>

      <div class="row item-body">
        {{ contents | safe }}
      </div>

      <div class="row item-body">

        <a href="https://twitter.com/share" class="twitter-share-button" data-via="landry_soules">Tweet</a>
        <script>
          !function (d, s, id) {
            var js,
              fjs = d.getElementsByTagName(s)[0],
              p = /^http:/.test(d.location)
                ? 'http'
                : 'https';
            if (!d.getElementById(id)) {
              js = d.createElement(s);
              js.id = id;
              js.src = p + '://platform.twitter.com/widgets.js';
              fjs.parentNode.insertBefore(js, fjs);
            }
          }(document, 'script', 'twitter-wjs');
          </script>
          <script src="//platform.linkedin.com/in.js" type="text/javascript"> lang: en_US</script>
          <script type="IN/Share" data-counter="right"></script>
          <!-- Place this tag where you want the +1 button to render. -->
    <div class="g-plusone" data-size="medium" data-annotation="inline" data-width="25"></div>

    <!-- Place this tag after the last +1 button tag. -->
    <script type="text/javascript">
    (function() {
      var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
      po.src = 'https://apis.google.com/js/platform.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
    })();
    </script>
 </div>

 <div id="disqus_thread"></div>
 <script>
 /**
 * RECOMMENDED CONFIGURATION VARIABLES: EDIT AND UNCOMMENT THE SECTION BELOW TO INSERT DYNAMIC VALUES FROM YOUR PLATFORM OR CMS.
 * LEARN WHY DEFINING THESE VARIABLES IS IMPORTANT: https://disqus.com/admin/universalcode/#configuration-variables
 */

 var disqus_config = function () {
 this.page.url = 'http://spaceinvade.rs/{{path}}'; // Replace PAGE_URL with your page's canonical URL variable
 this.page.identifier = {{ date|date('Ymd') }}; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
 };

 (function() {
    // DON'T EDIT BELOW THIS LINE
 var d = document, s = d.createElement('script');

 s.src = '//spaceinvaders.disqus.com/embed.js';

 s.setAttribute('data-timestamp', +new Date());
 (d.head || d.body).appendChild(s);
 })();
 </script>
 <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a></noscript>
    </div>
  </div>
</div>
{% endblock %}
