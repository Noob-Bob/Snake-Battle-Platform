<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%;">My Bot</span> <!--float-end right alignment-->
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">Create Bot</button>
                        
                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                            <div class="modal-content">
                                <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">Create Bot</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">Name</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-titl" placeholder="Input the name of the bot">
                                      </div>
                                      <div class="mb-3">
                                        <label for="add-bot-description" class="form-label">Description</label>
                                        <textarea v-model="botadd.description" class="form-control" id="add-bot-description" placeholder="Input the description of the bot"></textarea>
                                      </div>
                                      <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">Code</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            :theme="aceConfig.theme"
                                            style="height: 300px"
                                            :options="aceConfig.options" class="ace-editor" />
                                      </div>
                                </div>
                                <div class="modal-footer">
                                    <div class="error-message">{{ botadd.error_message }}</div>
                                    <button type="button" class="btn btn-primary" @click="add_bot">Save changes</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Creat Time</th>
                                    <th>Operations</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-model-' + bot.id">Modify</button>
                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">Delete</button>

                                        <!-- Modal -->
                                        <!-- each modal has a unique id (equal to bot.id) -->
                                        <div class="modal fade" :id="'update-bot-model-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                <h1 class="modal-title fs-5">Modify Bot</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">Name</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-titl" placeholder="Input the name of the bot">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-description" class="form-label">Description</label>
                                                        <textarea v-model="bot.description" class="form-control" id="add-bot-description" placeholder="Input the description of the bot"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-code" class="form-label">Code</label>
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            :theme="aceConfig.theme"
                                                            style="height: 300px"
                                                            :options="aceConfig.options" class="ace-editor" />

                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message">{{ bot.error_message }}</div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">Save changes</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                </div>
                                            </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from 'vue' // use reactive to bind a object
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap' // Modal api repo
import { VAceEditor } from 'vue3-ace-editor';
import "ace-builds/webpack-resolver";
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';


export default {
    components: {
        VAceEditor,
    },
    setup() {
        // reference: https://www.acwing.com/solution/content/176492/
        const aceConfig = reactive({
            theme: 'chrome', //主题
            arr: [
                /*所有主题*/
                "ambiance",
                "chaos",
                "chrome",
                "clouds",
                "clouds_midnight",
                "cobalt",
                "crimson_editor",
                "dawn",
                "dracula",
                "dreamweaver",
                "eclipse",
                "github",
                "gob",
                "gruvbox",
                "idle_fingers",
                "iplastic",
                "katzenmilch",
                "kr_theme",
                "kuroir",
                "merbivore",
                "merbivore_soft",
                "monokai",
                "mono_industrial",
                "pastel_on_dark",
                "solarized_dark",
                "solarized_light",
                "sqlserver",
                "terminal",
                "textmate",
                "tomorrow",
                "tomorrow_night",
                "tomorrow_night_blue",
                "tomorrow_night_bright",
                "tomorrow_night_eighties",
                "twilight",
                "vibrant_ink",
                "xcode",
            ],
            readOnly: false, //是否只读
            options: {
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true,
                tabSize: 2,
                showPrintMargin: false,
                fontSize: 16
            }
        });

        const store = useStore();
        let bots = ref([]);

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const refresh_bots = () => {
            $.ajax({
                url: "https://app2079.acapp.acwing.com.cn/api/user/bot/getlist/",
                // url: "http://127.0.0.1:3000/api/user/bot/getlist/",
                type: "get",
                headers: { // need auth if not public
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        }

        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "https://app2079.acapp.acwing.com.cn/api/user/bot/add/",
                // url: "http://127.0.0.1:3000/api/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add-bot-btn").hide(); // close the modal
                        refresh_bots();

                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            })
        }

        const update_bot = (bot) => {
            botadd.error_message = "";
            $.ajax({
                url: "https://app2079.acapp.acwing.com.cn/api/user/bot/update/",
                // url: "http://127.0.0.1:3000/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        Modal.getInstance('#update-bot-model-' + bot.id).hide();
                        refresh_bots();

                    } else {
                        bot.error_message = resp.error_message;
                    }
                }
            })
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "https://app2079.acapp.acwing.com.cn/api/user/bot/remove/",
                // url: "http://127.0.0.1:3000/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        refresh_bots();
                    }
                }
            })
        }

        const dataForm = reactive({
            textareashow: '{"A":"A1"}'
        });

        const jsonError = (e) => {
            console.log(`JSON字符串错误：${e.message}`);
        }

        const editorInit = () => {
            try {
                dataForm.textareashow = JSON.stringify(JSON.parse(dataForm.textareashow), null, 2)
            } catch (e) {
                jsonError(e)
            }
        };

        return {
            bots,
            botadd,
            add_bot,
            update_bot,
            remove_bot,
            editorInit,
            aceConfig,
        }
    }
}
</script>

<style scoped>
.error-message {
    color: red;
}
</style>