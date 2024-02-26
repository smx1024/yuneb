import Vue from 'vue'
import Vuex from 'vuex'


const now = new Date();

const store = new Vuex.Store({
	state: {
		routes: [],
		currentAdmin: JSON.parse(window.localStorage.getItem('user'))
	},
	getters: {

	},
	mutations: {
		initRoutes(state, data) {
			state.routes = data
		},
		initadmin(state, admin) {
			state.currentAdmin = admin
		}

	},
})


export default store;