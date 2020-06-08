<template>
<div>
    <el-container>
        <el-main>
            <div id="xterm"  />
        </el-main>
        <el-aside style="width:30%;text-align:center;">
            <p v-text="command" style="font-size:20px;"/>
            <el-table
            :data="desc"
            style="width: 100%;margin-buttom=20px;"
            >
            <el-table-column
                prop="command"
                label="命令"
                width="100">
            </el-table-column>
            <el-table-column
                prop="description"
                label="介绍"
                width="180">
            </el-table-column>
            </el-table>
            <br>
            <!-- <p id="help" style="width:100%;height:70%;border:1px solid #000;" v-text="desc"/> -->
            <el-button type="danger" round >我要纠错</el-button>
        </el-aside>
    </el-container>
    
</div>
</template>
<script>
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
import { AttachAddon } from 'xterm-addon-attach'
import store from '@/store'
import { getToken } from '@/utils/auth'
import { getCommandDesc } from '@/api/terminal'
import { getInfo } from '@/api/user'

export default {
  name: 'Xterm',
  props: {
    socketURI: {
      type: String,
      default: ''
    },
  },
  data(){
      return {
        message: "",
        sessionLogEnable: true,
        //rows:0,
        loading: true,
        path:"ws://localhost:8080/linuxterminal/10000/"+getToken(),
        socket:"",
        term: "",
        command:"欢迎参考",
        desc:[]
      }
  },
  mounted() {
    this.initSocket()
  },
  beforeDestroy() {
    this.socket.close()
    this.term.dispose()
  },
  watch: {
      'message' : function(newVal){
          var desc=newVal.split(' ')[0];
          if(desc!=''){
              getCommandDesc(desc).then(response=>{
                  //const { ,description} = response.data.object;
                  this.desc = response.object;
              }).catch(()=>{
                  this.desc = [{command:'出错！',description:'出错！'}]
              });
          }
      }
  },
  methods: {
    initTerm() {
        const term = new Terminal({
            fontSize: 14,
            rows:30,
            disableStdin: false, //是否应禁用输入。
            cursorBlink: true, // 光标闪烁
            cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
            scrollback: 800, //回滚
            tabStopWidth: 8, //制表宽度
            screenKeys: true,
            useStyle: true,
            convertEol: true
        });
        const attachAddon = new AttachAddon(this.socket);
        const fitAddon = new FitAddon();
        term.loadAddon(attachAddon);
        term.loadAddon(fitAddon);

        /* term.onData((data) =>{
                //键盘输入时的回调函数
            //term.write(data);
            term.write(data)
            if (new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ]").test(data)
                || new RegExp("[\\w]").test(data) || / /g.test(data)  ) {
                this.message = this.message + data
            }else{
                this.socket.send(data);
            }
            console.log(this.message)
        }); */
        
        /* term.attachCustomKeyEventHandler((ev)=>{
            if(ev.keyCode == 13){
                this.socket.send(this.message+'enter');
                this.message = "";   
            }
            
        }) */

        term.open(document.getElementById('xterm'));
        fitAddon.fit();
        term.focus();

        /* term.prompt = () => {
            term.write("\r\n ");
        };
        term.prompt(); */
        
        this.term = term
    },
    initSocket() {
      this.socket = new WebSocket(this.path);
      //this.socketOnMessage();
      this.socketOnClose();
      this.socketOnOpen();
      this.socketOnError();
    },
    socketOnOpen() {
      this.socket.onopen = () => {
        // 链接成功后
        this.initTerm() 
      }
    },
    socketOnMessage(){
        this.socket.onmessage = (msg) => {
            //this.rows = this.rows+1;
            /* console.log(msg.data)
            if(this.loading&& msg.data=="连接成功"){
                this.term.clear();
                this.term.writeln(msg.data);
                this.loading=false;
            }else{
                if(msg.data.indexOf("#")!=-1 && msg.data.indexOf("#")==msg.data.length-2){
                    this.term.write(msg.data)
                }else{
                    if(msg.data!= "")
                        this.term.writeln(msg.data)
                }
            } */
            
        }
    },
    socketOnClose() {
      this.socket.onclose = () => {
        // console.log('close socket')
      }
    },
    socketOnError() {
      this.socket.onerror = () => {
        // console.log('socket 链接失败')
      }
    },
    onSend(msg) {
        console.log(msg);
        //this.socket.send(msg);
    },
  }
}
</script>

<style scoped>

</style>