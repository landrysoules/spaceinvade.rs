---
layout: post.html
title: Make Mynt play well with Bootstrap
tags: [Mynt]
---

Mynt offers a lot of freedom to customize the look of your site/blog.
When moving to Mynt, my first task have been to make the default template more responsive, so I opted to adapt it to Bootstrap. The main issue I had (apart for being a total moron with everything html related !), have been with pygments. Indeed, pygments generates by default highlighted code in a html table, which generously overflows its parent div...
To solve this problem, you just have to change a parameter in mynt source code. It should be located in /path/to/your/mynt/virtualenv/lib/python2.7/site-packages/mynt
Edit core.py as follows:

~~~ { python }
    def _highlight(self, match):
            language, code = match.groups()
            #formatter = HtmlFormatter(linenos = 'table') replace table with inline
            formatter = HtmlFormatter(linenos = 'inline')
            code = h.unescape_html(code.encode('utf-8')).decode('utf-8')

            try:
                code = highlight(code, get_lexer_by_name(language), formatter)
            except ClassNotFound:
                code = highlight(code, get_lexer_by_name('text'), formatter)

            return '<div class="code"><div>{0}</div></div>'.format(code)
~~~

This simple change will tell pygments to generate highlighted code in span tags instead of table. 
