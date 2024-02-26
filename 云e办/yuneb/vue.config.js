
const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true
})
let proxyObj = {}
proxyObj['/'] = {
    //不代理websocket
    ws: false,
    //转发到的目标
    target: 'http://localhost:8081',
    //发送请求的请求头的host设置为target
    changeOrigin: true,
    //不重写请求地址
    pathRewrite: {
        '^/': '/'
    }
}
module.exports = {
    devServer: {
        host: 'localhost',
        port: 8080,
        proxy: proxyObj
    }
}
