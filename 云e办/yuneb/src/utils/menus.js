import {getRequest} from "./api";

export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }

    getRequest('/system/cfg/menu').then(data => {
        if (data) {
            //格式化Router
            let fmtRoutes = formatRoutes(data);
            //添加到router
           //vue-router4.0版本中，addRoutes方法已经被废弃，使用router.addRoute()方法代替
           fmtRoutes.forEach(route => {
                router.addRoute(route);
            })
        
            //将数据存入vuex中
            store.commit('initRoutes', fmtRoutes);
            //连接WebSocket
            store.dispatch('connect');
        }
    })

};

export const formatRoutes = (routes) => {
    let fmtRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            iconCls,
            children
        } = router;
        if (children && children instanceof Array) {
            //递归
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component: () => {
              if (component.startsWith('Home')) {
                return import(`../views/${component}.vue`);
              } else if (component.startsWith('Emp')) {
                return import(`../views/emp/${component}.vue`);
              } else if (component.startsWith('Per')) {
                return import(`../views/per/${component}.vue`);
              } else if (component.startsWith('Sal')) {
                return import(`../views/sal/${component}.vue`);
              } else if (component.startsWith('Sta')) {
                return import(`../views/sta/${component}.vue`);
              } else if (component.startsWith('Sys')) {
                return import(`../views/sys/${component}.vue`);
              }
            }
          };
          
        fmtRoutes.push(fmRouter);
    });
    return fmtRoutes;
};