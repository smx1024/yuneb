import axios from 'axios'
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
  // 创建axios实例
const downloadService = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: '',
  responseType: 'blob'
})

// request拦截器
downloadService.interceptors.request.use(config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  if (window.sessionStorage.getItem('tokenStr')) {
    config.headers['token'] = window.sessionStorage.getItem('tokenStr')// 让每个请求携带自定义token 请根据实际情况自行修改
  }
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?'
    for (const propName of Object.keys(config.params)) {
      const value = config.params[propName]
      var part = encodeURIComponent(propName) + '='
      if (value !== null && typeof (value) !== 'undefined') {
        if (typeof value === 'object') {
          for (const key of Object.keys(value)) {
            if (value[key] !== null && typeof (value[key]) !== 'undefined') {
              const params = propName + '[' + key + ']'
              const subPart = encodeURIComponent(params) + '='
              url += subPart + encodeURIComponent(value[key]) + '&'
            }
          }
        } else {
          url += part + encodeURIComponent(value) + '&'
        }
      }
    }
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  }
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

// 响应拦截器
downloadService.interceptors.response.use(res => {
  console.log(res)
  if (!res.data) {
    return
  }
  var blob = new Blob([res.data])  
  var fileName = 'test.xls'; // 设置默认文件名为 "员工表.xls"
  // 获取响应头中的 content-disposition
  var contentDisposition = res.headers['content-disposition'];
  console.log(contentDisposition)
  
  if (contentDisposition) {
    var match = contentDisposition.match(/filename="(.+)"/);
    if (match && match[1]) {
      fileName = decodeURIComponent(match[1]); // 使用正则表达式匹配获取文件名，并解码 URI 编码
    }
  }
  
  
  var url = window.URL.createObjectURL(blob)
  var aLink = document.createElement('a')
  aLink.style.display = 'none'
  aLink.href = url
  aLink.setAttribute('download', fileName)
  document.body.appendChild(aLink)
  aLink.click()
  document.body.removeChild(aLink) // 下载完成移除元素
  window.URL.revokeObjectURL(url) // 释放掉blob对象
},
error => {
    console.log('err' + error)
  return Promise.reject(error)
}
)
export default downloadService

