import Layout from '@/layout'

const managementRouter = [{
  path: '/',
  component: Layout,
  redirect: '/home',
  children: [{
    path: 'home',
    name: 'home',
    component: () => import('@/views/management/home/index'),
    meta: {
      title: '平台概览',
      icon: 'home'
    }
  }]
}, {
  path: '/merchantManage',
  component: Layout,
  redirect: '/index',
  children: [{
    path: 'index',
    name: 'index',
    component: () => import('@/views/management/merchantManage/index'),
    meta: {
      title: '商家管理',
      icon: 'merchant'
    }
  }]
}, {
  path: '/userManage',
  component: Layout,
  redirect: '/index',
  children: [{
    path: 'index',
    name: 'index',
    component: () => import('@/views/management/userManage/index'),
    meta: {
      title: '用户管理',
      icon: 'user'
    }
  }]
}, {
  path: '/orderManage',
  component: Layout,
  redirect: '/index',
  children: [{
    path: 'index',
    name: 'index',
    component: () => import('@/views/management/orderManage/index'),
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
    component: () => import('@/views/management/systemManage/adminManage/adminManage'),
    meta: {
      title: '管理员设置',
      icon: 'admin'
    }
  }, {
    path: 'couponTypeManage',
    name: 'CouponTypeManage',
    component: () => import('@/views/management/systemManage/couponTypeManage/couponTypeManage'),
    meta: {
      title: '优惠券类型管理',
      icon: 'coupon'
    }
  }, {
    path: 'serviceTypeManage',
    name: 'ServiceTypeManage',
    component: () => import('@/views/management/systemManage/serviceTypeManage/serviceTypeManage'),
    meta: {
      title: '服务类型管理',
      icon: 'service'
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
    component: () => import('@/views/management/logManage/webLog/index'),
    meta: {
      title: '网页端日志',
      icon: 'webLog'
    }
  }, {
    path: 'miniProgramLog',
    name: 'miniProgramLog',
    component: () => import('@/views/management/logManage/miniProgramLog/index'),
    meta: {
      title: '小程序日志',
      icon: 'miniProgramLog'
    }
  }]
},

// 404 page must be placed at the end !!!
{ path: '*', redirect: '/404', hidden: true }]

export default managementRouter
