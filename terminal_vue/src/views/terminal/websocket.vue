<template>    
	<div>       
		<div id="content"></div>
		请输入命令(按回车结束):<input type="text" id="message" v-model="message" 
		@keypress.enter="send" />
		<button id="send" @click="send"></button>
		<p id="msg" v-text="result"></p>
	</div>    
</template>
<script>
import store from '@/store'
import { getToken } from '@/utils/auth'
export default {        
	created() { // 页面创建生命周期函数
		//this.initWebSocket()
	}, 
	mounted () {
        // 初始化
        this.init()
    },       
	destroyed () {
        // 销毁监听
        this.socket.onclose = this.close
    },
	data(){
		return {
			message: "",
			result:"",
			path:"ws://localhost:8080/linuxterminal/10000/"+getToken(),
            socket:""
		}
	},
	methods: {            
		init: function () {
            if(typeof(WebSocket) === "undefined"){
                alert("您的浏览器不支持socket")
            }else{
                // 实例化socket
                this.socket = new WebSocket(this.path)
                // 监听socket连接
                this.socket.onopen = this.open
                // 监听socket错误信息
                this.socket.onerror = this.error
                // 监听socket消息
                this.socket.onmessage = this.getMessage
            }
        },
        open: function () {
            console.log("连接中……")
        },
        error: function (e) {
            console.log("连接错误")
        },
        getMessage: function (msg) {
            console.log(msg.data)
        },
        send: function () {
            this.socket.send(this.message);
			this.message = "";
        },
        close: function () {
            console.log("连接已经关闭")
        }

	 }    
}
</script>
<style lang="less" scoped>
</style>