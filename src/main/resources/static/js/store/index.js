import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: null,
    token: localStorage.getItem('token') || null
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user;
    },
    SET_TOKEN(state, token) {
      state.token = token;
      if (token) {
        localStorage.setItem('token', token);
      } else {
        localStorage.removeItem('token');
      }
    },
    CLEAR_AUTH(state) {
      state.user = null;
      state.token = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  },
  actions: {
    setUser({ commit }, user) {
      commit('SET_USER', user);
      localStorage.setItem('user', JSON.stringify(user));
    },
    setToken({ commit }, token) {
      commit('SET_TOKEN', token);
    },
    logout({ commit }) {
      commit('CLEAR_AUTH');
    },
    initializeStore({ commit }) {
      const token = localStorage.getItem('token');
      const user = localStorage.getItem('user');
      
      if (token) {
        commit('SET_TOKEN', token);
      }
      
      if (user) {
        commit('SET_USER', JSON.parse(user));
      }
    }
  },
  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    userRoles: state => state.user?.roles || []
  }
}); 