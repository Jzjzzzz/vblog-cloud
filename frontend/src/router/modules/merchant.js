import Layout from '@/layout'

const merchantRouter = [{
  path: '/',
  component: Layout,
  redirect: '/home',
  children: [{
    path: 'home',
    name: 'home',
    component: () => import('@/views/merchant/home/index'),
    meta: {
      title: '商家概览',
      icon: 'merchant'
    }
  }]
}, {
  path: '/orderManage',
  component: Layout,
  redirect: '/index',
  children: [{
    path: 'index',
    name: 'index',
    component: () => import('@/views/merchant/orderManage/index'),
    meta: {
      title: '订单管理',
      icon: 'order'
    }
  }]
}, {
  path: '/systemManage',
  component: Layout,
  redirect: '/adminManage',
  meta: {
    title: '系统设置',
    icon: 'setup'
  },
  children: [{
    path: 'adminManage',
    name: 'AdminManage',
    component: () => import('@/views/merchant/systemManage/adminManage/adminManage'),
    meta: {
      title: '管理员设置',
      icon: 'admin'
    }
  }]
}, {
  path: '/logManage',
  component: Layout,
  meta: {
    title: '操作日志',
    icon: 'log'
  },
  redirect: '/logManage/webLog',
  children: [{
    path: 'webLog',
    name: 'webLog',
    component: () => import('@/views/merchant/logManage/webLog/index'),
    meta: {
      title: '网页端日志',
      icon: 'webLog'
    }
  }, {
    path: 'miniProgramLog',
    name: 'miniProgramLog',
    component: () => import('@/views/merchant/logManage/miniProgramLog/index'),
    meta: {
      title: '小程序日志',
      icon: 'miniProgramLog'
    }
  }]
},

// 404 page must be placed at the end !!!
{ path: '*', redirect: '/404', hidden: true }]

export default merchantRouter
