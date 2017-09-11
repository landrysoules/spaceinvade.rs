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
  s3 = require('metalsmith-s3'),
  sass = require('metalsmith-sass'),
  excerpts = require('metalsmith-better-excerpts'),
  feed = require('metalsmith-feed'),
  debug = require('metalsmith-debug')

var now = new Date()

metalsmith(__dirname)
  .use(debug())
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
  .use(markdown({
    langPrefix: 'language-'
  }))
  .use(excerpts({
    stripTags: true,
    pruneLength: 400,
    pruneString: '…'
  }))
  .use(permalinks({
    pattern: ':collection/:title'
  }))
  .use(prism)
  .use(define({
    site: {
      title: 'spaceinvade.rs',
      url: 'http://spaceinvade.rs',
      author: 'Landry Soules',
      sub: 'Programming and rock n\' roll!'
    },
    now: now
  }))
  .use(feed({
    collection: 'posts'
  }))
  .use(layouts({
    engine: 'swig'
  }))
  .use(msIf(!process.env.METAL_ENV || process.env.METAL_ENV == 'DEV',
       browserSync({
    server: 'build',
    files: ['src/**/*.md', 'layouts/**/*.swig']
  })))
  // .use(msIf(
  //   process.env.AWS,
  //   s3({
  //     action: 'write',
  //     bucket: 'spaceinvade.rs',
  //     region: 'eu-west-1'
  //   }) // this plugin will run
  // ))
  .destination('./build')
  .use(sass({
    file: 'css/space.scss',
    outputDir: 'css/'
      // outputStyle: "expanded",
      // sourceMap: true,
      // sourceMapContents: true
  }))
  .use(msIf(
    process.env.AWS,
    s3({
      action: 'write',
      bucket: 'spaceinvade.rs',
      region: 'eu-west-1'
    }) // this plugin will run
  ))
  .build(function(err) {
    if (err) {
      console.log(err)
      throw err
    }
  })
