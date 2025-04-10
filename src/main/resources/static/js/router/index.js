import Vue from 'vue';
import VueRouter from 'vue-router';
import { authGuard, adminGuard, authorGuard, editorGuard, userGuard, roleGuard } from './guards';
import store from '../store';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../components/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../components/auth/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../components/auth/Register.vue')
  },
  {
    path: '/books',
    name: 'Books',
    component: () => import('../components/Books.vue')
  },
  {
    path: '/authors',
    name: 'Authors',
    component: () => import('../components/Authors.vue')
  },
  {
    path: '/categories',
    name: 'Categories',
    component: () => import('../components/Categories.vue')
  },
  {
    path: '/basket',
    name: 'Basket',
    component: () => import('../components/Basket.vue'),
    beforeEnter: userGuard
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../components/Orders.vue'),
    beforeEnter: userGuard
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../components/Profile.vue'),
    beforeEnter: userGuard
  },
  // Routes pour les auteurs
  {
    path: '/dashboard/authors',
    name: 'AuthorDashboard',
    component: () => import('../components/author/AuthorDashboard.vue'),
    beforeEnter: authorGuard
  },
  {
    path: '/dashboard/authors/stats',
    name: 'AuthorStats',
    component: () => import('../components/author/AuthorStats.vue'),
    beforeEnter: authorGuard
  },
  // Routes pour les éditeurs
  {
    path: '/dashboard/editors',
    name: 'EditorDashboard',
    component: () => import('../components/editor/EditorDashboard.vue'),
    beforeEnter: editorGuard
  },
  {
    path: '/dashboard/editors/stats',
    name: 'EditorStats',
    component: () => import('../components/editor/EditorStats.vue'),
    beforeEnter: editorGuard
  },
  // Routes pour les administrateurs
  {
    path: '/dashboard',
    name: 'AdminDashboard',
    component: () => import('../components/admin/AdminDashboard.vue'),
    beforeEnter: (to, from, next) => {
      roleGuard(['ADMIN', 'EDITOR'])(to, from, next);
    }
  },
  {
    path: '/dashboard/users',
    name: 'UserManagement',
    component: () => import('../components/admin/UserManagement.vue'),
    beforeEnter: adminGuard
  },
  {
    path: '/dashboard/authors',
    name: 'AuthorManagement',
    component: () => import('../components/admin/AuthorManagement.vue'),
    beforeEnter: authorGuard
  },
  {
    path: '/dashboard/editors',
    name: 'EditorManagement',
    component: () => import('../components/admin/EditorManagement.vue'),
    beforeEnter: editorGuard
  },
  {
    path: '/dashboard/categories',
    name: 'CategoryManagement',
    component: () => import('../components/admin/CategoryManagement.vue'),
    beforeEnter: adminGuard
  },
  {
    path: '/dashboard/books',
    name: 'BookManagement',
    component: () => import('../components/admin/BookManagement.vue'),
    beforeEnter: editorGuard
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: () => import('../components/Unauthorized.vue')
  },
  {
    path: '*',
    redirect: '/'
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

router.beforeEach((to, from, next) => {
  // Initialiser le store au chargement de l'application
  if (!store.getters.isInitialized) {
    store.dispatch('initializeStore');
  }
  
  // Liste des routes qui nécessitent une authentification
  const authRequiredRoutes = [
    '/dashboard', 
    '/dashboard/users', 
    '/dashboard/authors', 
    '/dashboard/editors',
    '/dashboard/categories',
    '/dashboard/books'
  ];
  
  // Vérifier si la route actuelle commence par une des routes protégées
  const requiresAuth = authRequiredRoutes.some(route => 
    to.path === route || to.path.startsWith(route + '/')
  );
  
  // Si la route nécessite une authentification et que l'utilisateur n'est pas connecté
  if (requiresAuth && !store.getters.isAuthenticated) {
    console.log('Redirection vers login: utilisateur non authentifié pour', to.path);
    return next('/login');
  }
  
  // Vérification des rôles pour les routes spécifiques
  const userRoles = store.getters.userRoles || [];
  console.log('Rôles utilisateur actuels:', userRoles);
  
  // Mappings des routes vers les rôles requis
  const roleRequirements = {
    '/dashboard/users': ['ADMIN'],
    '/dashboard/categories': ['ADMIN'],
    '/dashboard/authors': ['ADMIN', 'AUTHOR'],
    '/dashboard/editors': ['ADMIN', 'EDITOR'],
    '/dashboard/books': ['ADMIN', 'EDITOR'],
    '/dashboard': ['ADMIN', 'EDITOR']
  };
  
  // Vérifier si la route actuelle a des exigences spécifiques de rôle
  for (const [route, requiredRoles] of Object.entries(roleRequirements)) {
    if (to.path === route || to.path.startsWith(route + '/')) {
      const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));
      
      if (!hasRequiredRole) {
        console.log('Redirection vers unauthorized: rôle insuffisant pour', to.path);
        console.log('Rôles requis:', requiredRoles, 'Rôles utilisateur:', userRoles);
        return next('/unauthorized');
      }
      
      break;
    }
  }
  
  next();
});

export default router;