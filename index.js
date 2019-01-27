'use strict'

var metalsmith = require('metalsmith'),
  markdown = require('metalsmith-markdown'),
  layouts = require('metalsmith-layouts'),
  collections = require('metalsmith-collections'),
  permalinks = require('metalsmith-permalinks'),
  browserSync = require('metalsmith-browser-sync'),
  prism = require('metalsmith-prism'),
  ignore = require('metalsmith-ignore'),
  define = require('metalsmith-define'),
  msIf = require('metalsmith-if'),
  sass = require('metalsmith-sass'),
  feed = require('metalsmith-feed'),
  debug = require('metalsmith-debug'),
  snippet = require('metalsmith-snippet'),
  metalsmithExpress = require('metalsmith-express');

var now = new Date()

metalsmith(__dirname)
  .use(debug())
  .use(msIf(process.env.METAL_ENV == 'PROD', metalsmithExpress()))
  .use(ignore(['content/drafts/*']))
  .use(collections({
    pages: {
      pattern: 'content/pages/*.md'
    },
    posts: {
      pattern: 'content/posts/*.md',
      sortBy: 'date',
      reverse: true
    }
  }))
  .use(markdown({langPrefix: 'language-'}))
  .use(permalinks({pattern: ':collection/:title'}))
  .use(prism)
  .use(snippet({maxLength: 400, stripPre: true, stop: ['<code']}))
  .use(define({
    site: {
      title: 'spaceinvade.rs',
      url: 'http://spaceinvade.rs',
      author: 'Landry Soules',
      sub: 'Programming and rock n\' roll!'
    },
    now: now
  }))
  .use(feed({collection: 'posts'}))
  .use(layouts({engine: 'swig'}))
  .use(msIf(!process.env.METAL_ENV || process.env.METAL_ENV == 'DEV', browserSync({
    server: 'build',
    files: ['src/**/*.md', 'layouts/**/*.swig']
  })))
  .destination('./build')
  .use(sass({
    file: 'sass/space.scss', includePaths: ['sass'], outputDir: 'css/'
    // outputStyle: "expanded", sourceMap: true, sourceMapContents: true
  }))
  .build(function (err) {
    if (err) {
      console.log(err)
      throw err
    }
  })
