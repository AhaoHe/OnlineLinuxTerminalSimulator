<template>
<div>
    <el-container>
        <el-main>
            <div id="xterm"  />
        </el-main>
        <el-main style="width:30%;text-align:center;background-color: white">
            <el-tabs type="border-card">
                <el-tab-pane label="参考相关命令">
                    <!-- <el-card style="width:100%;"> -->
                    <!-- <p v-text="command" style="font-size:20px;"/> -->
                    <el-table
                    :data="desc"
                    style="width: 100%;margin-buttom=20px;"
                    >
                    <el-table-column
                        prop="command"
                        label="命令"
                        fixed="left"
                        width="80%">
                    </el-table-column>
                    <el-table-column
                        prop="description"
                        label="介绍"
                        width="200%">
                    </el-table-column>
                    </el-table>
                    <br>
                    <!-- <p id="help" style="width:100%;height:70%;border:1px solid #000;" v-text="desc"/> -->
                    <el-button type="danger" round >我要纠错</el-button>
                    <!-- </el-card> -->
                </el-tab-pane>
                <el-tab-pane label="组成员列表">
                    <group-users></group-users>
                </el-tab-pane>
            </el-tabs>
            
        </el-main>
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
import GroupUsers from './components/groupusers'

export default {
  name: 'Xterm',
  props: {
    socketURI: {
      type: String,
      default: ''
    },
  },
  components:{GroupUsers},
  data(){
      return {
        message: "",
        sessionLogEnable: true,
        record:[],
        index: 0 ,
        //rows:0,
        loading: true,
        path:"ws://localhost:8080/linuxterminal/10000/"+getToken(),
        socket:"",
        term: "",
        //command:"欢迎参考",
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
          if(desc!=''&& desc.indexOf('')==-1){
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
        //const attachAddon = new AttachAddon(this.socket);
        const fitAddon = new FitAddon();
        //term.loadAddon(attachAddon);
        term.loadAddon(fitAddon);

        term.onData((data) =>{
                //键盘输入时的回调函数
            //term.write(data);
            term.write(data)
            if (new RegExp("[`\\-~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ]").test(data)
                || new RegExp("[\\w]").test(data) || / /g.test(data)  ) {
                    if(data.indexOf('')==-1)
                        this.message = this.message + data
            }else{
                this.socket.send(data);
            }
            console.log(this.message)
        });
        term.onKey(ev =>{
            if(ev.domEvent.keyCode == 8){
                if(this.message!="")
                    term.write('\b \b')
                if(this.message.trim().length<2){
                    this.message = "";
                }
                else{
                    this.message = this.message.substring(0,this.message.length-1);
                    /* console.log(this.message)
                    console.log(this.message.length) */
                }
                //term.refresh(this.rows-1,this.rows)
            }else if(ev.domEvent.keyCode == 38){
                //向上按钮
                if(this.index != -1)
                    this.index--;
                if(this.index != -1 && this.record[this.index]!= undefined){
                    //将记录的命令行传给message
                    this.message= this.record[this.index]
                    //删除前面展示的记录
                    if(this.index!=this.record.length-1){
                        let back='\b \b';
                        let len = this.record[this.index+1].length;
                        for(var i = 0;i<len-1 ;i++){
                            back+=back;
                        }
                        term.write(back);
                    }
                    //展示现在的记录
                    term.write(this.message);
                }
                ev.domEvent.returnValue=false;
                throw console.error("阻止向上按键");
            }else if(ev.domEvent.keyCode == 40){
                //向下按钮  error
                if(this.index != this.record.length-1)
                    this.index++;
                if(this.index != -1 && this.record[this.index]!= undefined){
                    //将记录的命令行传给message
                    this.message= this.record[this.index]
                    //删除前面展示的记录
                    let back='\b \b';
                    let len = this.record[this.index-1].length;
                    for(var i = 0;i<len-1 ;i++){
                        back+=back;
                    }
                    term.write(back);
                    //展示现在的记录
                    term.write(this.message);
                }
                throw console.error("阻止向下按键");
            }
        })
        term.attachCustomKeyEventHandler((ev)=>{
            if(ev.keyCode == 13){
                if(this.message!=''){
                    this.record.push(this.message);
                    this.index = this.record.length;
                }
                console.log(this.record);
                if(this.message=='clear'){
                    this.message='';
                    term.clear();
                    this.socket.send('key_enter');
                    term.writeln('成功！')
                }else{
                    this.socket.send(this.message+'\r\n');
                    this.message = "";   
                }
            }
            
        })

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
      this.socketOnMessage();
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
            console.log(msg.data)
            if(this.loading&& msg.data=="连接成功"){
                this.term.clear();
                this.term.writeln(msg.data);
                this.loading=false;
            }else{
                if(msg.data.indexOf("#")!=-1 && msg.data.indexOf("#")==msg.data.length-2){
                    this.term.write(msg.data)
                }else if(msg.data.indexOf("$")!=-1 && msg.data.indexOf("$")==msg.data.length-2){
                    this.term.write(msg.data)
                }else{
                    if(msg.data!= "")
                        this.term.writeln(msg.data)
                }
            }
            
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