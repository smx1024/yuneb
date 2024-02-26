import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import  downloadService  from '@/utils/download.js'

//请求拦截器
axios.interceptors.request.use(config=>{
    //如果存在token，则请求携带这token
    if(window.sessionStorage.getItem('tokenStr')){
        config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
    }
    return config;
},error => {
    console.log(error);
})

//响应拦截器
axios.interceptors.response.use(success=>{
    //业务逻辑错误
    if(success.status && success.status==200){
        if(success.data.code==500 || success.data==401 || success.data==403||success.data.code==20001){
            ElMessage.error({message:success.data.message});
            // ElMessage.error("业务逻辑错误！");
            return;
        }
        if(success.data.message){
            ElMessage.success(success.data.message);
            // ElMessage.success("业务逻辑成功！");
        }
    }
    return success.data;
},error => {
    if(error.response.code==504 || error.response.code==404){
        ElMessage.error('服务器被吃了(⊙﹏⊙)');
    }else if(error.response.code==403){
        ElMessage.error('权限不足，请联系管理员！');
    }else if(error.response.code==401){
        ElMessage.error('尚未登陆，请登录！');
        router.replace('/');
    }else{
        if(error.response.data.message){
            ElMessage.error(error.response.data.message);
        }else{
            ElMessage.error('未知错误！');
        }
    }
    return;
});
let base = '';

//传送json格式的post请求
export const postRequest =(url,params)=>{
    return axios({
        method:'post',
        url:`${base}${url}`,
        data:params
    })
}

//传送json格式的put请求
export const putRequest = (url,params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}

//传送json格式的get请求
export  const getRequest = (url,params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

//传送json格式的delete请求
export  const deleteRequest = (url,params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}


// 导出分类
export function exportEmp(url, params) {
    return downloadService({
      url: `${base}${url}`,
      method: 'get',
      data: params
    })
  }
  