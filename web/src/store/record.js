// import $ from 'jquery';
export default {
    state: {
        is_record: false, // whether it is a record
        a_steps: "",
        b_steps: "",
        record_loser: "",
    },
    getters: {
    },
    mutations: {
        // sync operations should be put in mutations
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        }
    }, 
    actions: {
        
    },
    modules: {

    }
}
