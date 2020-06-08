const path = require('path')
const defaultSettings = require('./src/settings.js')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title || '在线Linux终端平台' // page title

module.exports={
    devServer: {
        port: 8888,     // 端口
        /* proxy: {
            '/': {
                target: 'http://localhost:8080/',
                ws: true,
                changeOrigin: true
            }
        } */
        // proxy: {
        //     '/socket.io': {
        //      target: 'http://localhost:8080',
        //      ws: true,
        //      changeOrigin: true
        //     },
        //     'sockjs-node': {
        //      target: 'http://localhost:8080',
        //      ws: false,
        //      changeOrigin: true
        //     },
        //    }
    },
    lintOnSave:false,
    publicPath: '/',
    configureWebpack: {
        name: name,
        resolve: {
            extensions: ['.js','.vue','.json'],
            alias: {
                'assets': '@/assets',
                'components': '@/components',
                'views': '@/views',
                '@': resolve('src'),
            }
        }
      },
      chainWebpack(config) {
          
        // set svg-sprite-loader
        config.module
            .rule('svg')
            .exclude.add(resolve('src/icons'))
            .end()
        config.module
            .rule('icons')
            .test(/\.svg$/)
            .include.add(resolve('src/icons'))
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
            symbolId: 'icon-[name]'
            })
            .end()
      }
}