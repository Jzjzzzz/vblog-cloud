import router from './router'
import store from './store'
import {
  Message
} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {
  getToken
} from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

import merchantRouter from '@/router/modules/merchant'
import managementRouter from '@/router/modules/management'
import {
  constantRoutes
} from '@/router'

NProgress.configure({
  showSpinner: false
}) // NProgress Configuration

const whiteList = ['/merchant/login', '/management/login'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  const managementInfo = sessionStorage.getItem('managementInfo')
  const merchantInfo = sessionStorage.getItem('merchantInfo')

  if (whiteList.indexOf(to.path) !== -1) {
    // in the free login whitelist, go directly
    next()
  } else {
    if (to.path === '/login') {
      // other pages that do not have permission to access are redirected to the login page.
      if (managementInfo === 'true') {
        next(`/management/login`)
      } else {
        next(`/merchant/login`)
      }
    } else {
      let allow = true
      const length = router.options.routes.length
      if (length === constantRoutes.length && managementInfo === 'true') {
        router.addRoutes(managementRouter)
        router.options.routes = router.options.routes.concat(managementRouter)
        allow = false
        next({ ...to,
          replace: true
        })
      }
      if (length === constantRoutes.length && merchantInfo === 'true') {
        router.addRoutes(merchantRouter)
        router.options.routes = router.options.routes.concat(merchantRouter)
        allow = false
        next({ ...to,
          replace: true
        })
      }
      if (allow) {
        if (length === constantRoutes.length) {
          next(`/merchant/login`)
        }
        next()
      }
    }
  }
  NProgress.done()
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
