import {createRouter, createWebHashHistory} from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'



const routes = [
    {
        path: '/',
        name: 'login',
        component: Login,
        hidden: true
    },
    {
        path: '/home',
        name: '首页',
        component: Home,
        hidden: true,
        children: [
            {
                path: '/home/chat',
                name: '聊天室',
                component: () => import('../views/chat/chat.vue'),
            },
            {
                path:'/userinfo',
                name:'userinfo',
                component:()=>import('../views/userinfo.vue')
            }
        ]
    },
    {
        path: '/emp',
        name: '员工管理',
        component: Home,
        hidden: true,
        children: [
            {
                path: '/emp/basic',
                name: '基本资料123',
                component: () => import('../views/emp/EmpBasic.vue'),
                children: []
            },
            {
                path: '/emp/adv',
                name: '高级资料',
                component: () => import('../views/emp/EmpAdv.vue'),
                children: []
            }
        ]
    },
    // {
    //     path: '/per',
    //     name: '人事管理',
    //     component: Home,
    //     hidden: true,
    //     children: [
    //         {
    //             path: '/per/emp',
    //             name: '员工资料',
    //             component: () => import('../views/per/PerEmp.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/per/ec',
    //             name: '员工奖惩',
    //             component: () => import('../views/per/PerEc.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/per/train',
    //             name: '员工培训',
    //             component: () => import('../views/per/PerTrain.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/per/salary',
    //             name: '员工调薪',
    //             component: () => import('../views/per/PerSalary.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/per/move',
    //             name: '员工调动',
    //             component: () => import('../views/per/PerMove.vue'),
    //             hidden: true,
    //             children: []
    //         }
    //     ]
    // },
    // {
    //     path: '/sal',
    //     name: '薪资管理',
    //     component: Home,
    //     hidden: true,
    //     children: [
    //         {
    //             path: '/sal/sob',
    //             name: '工资账套管理',
    //             component: () => import('../views/sal/SalSob.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sal/sobcfg',
    //             name: '员工账套设置',
    //             component: () => import('../views/sal/SalSobCfg.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sal/table',
    //             name: '工资表管理',
    //             component: () => import('../views/sal/SalTable.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sal/month',
    //             name: '月末处理',
    //             component: () => import('../views/sal/SalMonth.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sal/search',
    //             name: '工资表查询',
    //             component: () => import('../views/sal/SalSearch.vue'),
    //             hidden: true,
    //             children: []
    //         }
    //     ]
    // },
    // {
    //     path: '/sta',
    //     name: '统计管理',
    //     component: Home,
    //     hidden: true,
    //     children: [
    //         {
    //             path: '/sta/all',
    //             name: '综合信息统计',
    //             component: () => import('../views/sta/StaAll.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sta/score',
    //             name: '员工积分统计',
    //             component: () => import('../views/sta/StaScore.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sta/pers',
    //             name: '人事信息统计',
    //             component: () => import('../views/sta/StaPers.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sta/record',
    //             name: '人事记录统计',
    //             component: () => import('../views/sta/StaRecord.vue'),
    //             hidden: true,
    //             children: []
    //         }
    //     ]
    // },
    // {
    //     path: '/sys',
    //     name: '系统管理',
    //     component: Home,
    //     hidden: true,
    //     children: [
    //         {
    //             path: '/sys/basic',
    //             name: '基础信息设置',
    //             component: () => import('../views/sys/SysBasic.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sys/config',
    //             name: '系统设置',
    //             component: () => import('../views/sys/SysConfig.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sys/log',
    //             name: '操作日志管理',
    //             component: () => import('../views/sys/SysLog.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sys/admin',
    //             name: '操作员管理',
    //             component: () => import('../views/sys/SysAdmin.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sys/data',
    //             name: '备份恢复数据库',
    //             component: () => import('../views/sys/SysData.vue'),
    //             hidden: true,
    //             children: []
    //         },
    //         {
    //             path: '/sys/init',
    //             name: '初始化数据库',
    //             component: () => import('../views/sys/SysInit.vue'),
    //             hidden: true,
    //             children: []
    //         }
    //     ]
    // }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
