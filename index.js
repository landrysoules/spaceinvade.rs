'use strict'

var metalsmith = require('metalsmith'),
  markdown = require('metalsmith-markdown'),
  layouts = require('metalsmith-layouts'),
  collections = require('metalsmith-collections'),
  permalinks = require('metalsmith-permalinks'),
  browserSync = require('metalsmith-browser-sync'),
  prism = require('metalsmith-prism'),
  ignore = require('metalsmith-ignore'),
  snippet = require('metalsmith-snippet'),
  define = require('metalsmith-define')
  // fs = require('fs')

var now = new Date()

metalsmith(__dirname)
  .use(ignore(['content/drafts/*']))
  .use(collections({
    pages: {
      pattern: 'content/pages/*.md'
    },
    posts: {
      pattern: 'content/posts/*.md'
        // sortBy: 'date',
        // reverse: true
    }
  }))
  .use(markdown({
    langPrefix: 'language-'
  }))
  .use(permalinks({
    pattern: ':collection/:title'
  }))
  .use(prism)
  .use(define({
    site: {
      title: 'spaceinvade.rs',
      author: 'Landry Soules'
    },
    now: now
  }))
  .use(layouts({
    engine: 'swig'
  }))
  .use(snippet({
    maxLength: 200
  }))
  .use(browserSync({
    server: 'build',
    files: ['src/**/*.md', 'layouts/**/*.swig']
  }))
  .destination('./build')
  .build(function(err) {
    if (err) {
      console.log(err)
      throw err
    }
  })
