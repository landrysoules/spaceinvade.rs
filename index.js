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
  define = require('metalsmith-define'),
  msIf = require('metalsmith-if'),
  s3 = require('metalsmith-s3'),
  sass = require('metalsmith-sass')
  // fs = require('fs')

var now = new Date()

metalsmith(__dirname)
.use(sass({
  file: 'css/_bootstrap.scss',
    outputStyle: "expanded",
    sourceMap: true,
  sourceMapContents: true
  }
))
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
      author: 'Landry Soules',
      sub: 'Programming and rock n\' roll!'
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
  .use(msIf(
    process.env.AWS,
    s3({
      action: 'write',
      bucket: 'spaceinvade.rs',
      region: 'eu-west-1'
    }) // this plugin will run
  ))
  .destination('./build')

    // {
    // includePaths: [
    //   'src/css'
    // ]
    // file: 'src/css/_bootstrap.scss',
    // outputDir: 'css/' // This changes the output dir to "build/css/" instead of "build/scss/"
    // }

  .build(function(err) {
    if (err) {
      console.log(err)
      throw err
    }
  })
