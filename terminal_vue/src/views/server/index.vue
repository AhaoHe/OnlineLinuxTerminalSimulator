<template>
<div style="margin-left:20px">
    <div style="margin-bottom:20px;margin-top:20px">服务器列表
      <el-button type="primary" size="mini" style="margin-right:10px;float:right" @click="addenable=true;setServer(null)">新增服务器</el-button>
    </div>
    
    <el-row :gutter="20" class="panel-group">
    <el-col v-for="(o) in data" :key="o.id" :offset="0" :xs="12" :sm="22" :lg="7" class="card-panel-col">
        <el-card :body-style="{ padding: '0px' }">
        <div style="padding: 14px;">
            <span style="margin-right:10px;" v-text="o.id+'号服务器'"></span>
            <el-tag v-text="o.enable==true?'连接成功':'连接失败'" :type="o.enable==true?'':'danger'" size="mini"></el-tag>
            <el-tag style="margin-left:10px;" v-if="o.registerEnable==3?true:false" type="success" size="mini">默认注册</el-tag>
            <div class="bottom clearfix">
              <span style="margin-right:10px;">IP:</span><span v-text="o.hostname+':'+o.port" style="font-size: 14px;color: #999;"></span>
            </div>
            <div class="bottom clearfix">
              <span style="margin-right:10px;">磁盘分配:</span><span v-text="o.disk" style="font-size: 14px;color: #999;"></span>
            </div>
            <div class="bottom clearfix">
              <time class="time">{{ getStatus(o.status) }}</time>
              <el-button type="text" class="button" style="color:red;" @click="deleteData(o.id)">删除</el-button>
              <el-button type="text" class="button" style="margin-right:10px;" @click="setServer(o.id)">修改</el-button>
              <el-button type="text" v-if="o.registerEnable!=3?true:false" class="button" 
              style="color:green;" @click="setRegister(o.id)">设为默认</el-button>
            </div>
        </div>
        </el-card>
    </el-col>
    </el-row>
    <!-- 编辑 -->
    <el-dialog :title="'编辑信息-'+server.id+'号服务器'" :visible.sync="visable" width="400px">
          <el-form ref="editForm" :model="server" :rules="rules" label-width="100px" class="resiger-box">
          <el-form-item label="主机IP" prop="hostname">
              <el-input type="text" style="width: 80%;" placeholder="请输入ip" v-model="server.hostname"/>
          </el-form-item>
          <el-form-item label="端口" prop="port">
              <el-input type="text" style="width: 80%;" placeholder="请输入端口" v-model="server.port"/>
          </el-form-item>
          <el-form-item label="磁盘分配" prop="disk">
            <el-select v-model="server.disk" placeholder="请选择">
                <el-option
                v-for="item in selection"
                :key="item.value"
                :label="item.text"
                :value="item.value">
                </el-option>
            </el-select>          
          </el-form-item>
          <el-form-item label="登录方式" >
              <el-radio-group v-model="server.status" >
                  <el-radio :label="0" :value="0">密码(不推荐)</el-radio>
                  <el-radio :label="1" :value="1">库密钥</el-radio>
                  <el-radio :label="2" :value="2">文件密钥</el-radio>
              </el-radio-group>
          </el-form-item>
          <el-form-item label="是否重置">
              <el-switch v-model="server.enable" style="margin-right:10px"></el-switch>
              <el-input v-if="server.enable&&server.status==0" placeholder="请输入密码" type="password" style="width: 60%;" v-model="server.rootPassword" />
              <el-input v-if="server.enable&&server.status==1" placeholder="请输入密钥" type="textarea" style="width: 60%;" v-model="server.sqlkey" />
              <el-upload
                v-if="server.enable&&server.status==2"
                class="upload-demo"
                ref="upload"
                name="file"
                action="http://localhost:8080/server/file"
                :file-list="fileList"
                :multiple="false"
                :headers="Token()"
                :data="{id:this.server.id}"
                :limit="1"
                :on-success="onSuccess"
                :on-error="onError"
                :auto-upload="false">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">上传密钥文件</div>
              </el-upload>
          </el-form-item>
          </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visable = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="editData('editForm')">修 改</el-button>
      </div>
    </el-dialog>
    <!-- 新增 -->
    <el-dialog :title="'新增服务器'" :visible.sync="addenable" width="400px">
          <el-form ref="addForm" :model="server" :rules="rules" label-width="100px" class="resiger-box">
          <el-form-item label="主机IP" prop="hostname">
              <el-input type="text" style="width: 80%;" placeholder="请输入ip" v-model="server.hostname"/>
          </el-form-item>
          <el-form-item label="端口" prop="port">
              <el-input type="text" style="width: 80%;" placeholder="请输入端口" v-model="server.port"/>
          </el-form-item>
          <el-form-item label="磁盘分配" prop="disk">
            <el-select v-model="server.disk" placeholder="请选择">
                <el-option
                v-for="item in selection"
                :key="item.value"
                :label="item.text"
                :value="item.value">
                </el-option>
            </el-select>          
          </el-form-item>
          <el-form-item label="登录方式" >
              <el-radio-group v-model="server.status" >
                  <el-radio :label="0" :value="0">密码(不推荐)</el-radio>
                  <el-radio :label="1" :value="1">库密钥</el-radio>
                  <el-radio :label="2" :value="2">文件密钥</el-radio>
              </el-radio-group>
          </el-form-item>
          <el-form-item required>
              <el-input v-if="server.status==0" placeholder="请输入密码" type="password" style="width: 60%;" v-model="server.rootPassword" />
              <el-input v-if="server.status==1" placeholder="请输入密钥" type="textarea" style="width: 60%;" v-model="server.sqlkey" />
              <el-upload
                v-if="server.status==2"
                class="upload-demo"
                ref="uploadADD"
                name="file"
                action="http://localhost:8080/server/file"
                :file-list="fileList"
                :multiple="false"
                :headers="Token()"
                :data="{id:0}"
                :limit="1"
                :on-success="onSuccess"
                :on-error="onError"
                :auto-upload="false">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">上传密钥文件</div>
              </el-upload>
          </el-form-item>
          </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addenable = false">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="addData('addForm')">添 加</el-button>
      </div>
    </el-dialog>
</div>
</template>
<script>
  import { getServerInfo , addServerInfo , updateServerInfo , deleteServerInfo , setRegister } from '@/api/server'
  import { Message } from 'element-ui';
  import { getToken } from '@/utils/auth'
export default {
  mounted(){
    this.getDataInfo();
  },
  data() {
    var validateHostname = (rules, value, callback) => {
        if(value=='') callback(new Error('主机IP不可为空!'))
        var patrn=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
        if (!patrn.exec(value)){
            callback(new Error('ip格式错误!'));
        }else{
          callback()
        }
    };
    var validatePort = (rules, value, callback) => {
            if(value=='') callback(new Error('端口号不能为空!'))
            var patrn=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;  
            if (!patrn.exec(value)) callback(new Error('端口格式错误'));
            else callback()
        }
    return {
      data: [],
      fileList:[],
      visable:false,
      addenable:false,
      server: {},
      loading:false,
      rules:{
        hostname: [
            { validator: validateHostname, trigger: 'blur' },
            {required: true, message: '主机IP不可为空', trigger: 'blur'},
            //{ min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' }
        ],
        port: [
            { validator: validatePort, trigger: 'blur' },
            {required: true, message: '端口号不可为空', trigger: 'blur'},
            //{ min: 1, max: 6, message: '长度在 1 到 6 个数字', trigger: 'blur' }
        ],
        disk:[
          { required: true, message: '请选择磁盘分配', trigger: 'change' }
        ]
      },
      selection:[{text:'10M',value:'10M'},{text:'20M',value:'20M'},{text:'50M',value:'50M'},{text:'100M',value:'100M'}]
    };
  },
  methods:{
    Token(){
      let token =getToken();
      return {Authorization:token}
    },
    getDataInfo(){
      getServerInfo().then(response=>{
        this.data = response.object;
        console.log(response.object)
      }).catch(()=>{
        Message.error("获取出错！");
      })
    },
    getStatus(status){
      switch(status){
          case 0:
            return "密码登录"
            break;
          case 1:
            return "库密钥登录"
            break;
          case 2:
            return "文件密钥登录"
            break;
      }
    },
    setRegister(id){
      var item = this.data.find(item=>item.id==id);
      if(!item.enable){
        Message.error("连接失败的服务器无法设为默认服务器！")
      }else{
        setRegister(id).then(response=>{
          this.data = response.object
        }).catch(()=>{
          Message.error("设置默认出错！");
        })
      }
    },
    deleteData(id){
      this.$confirm(`是否删除该服务器?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        deleteServerInfo(id).then(response=>{
          if(response.object==false){
            Message.error("存在用户，无法删除");
          }else{
            this.data = response.object
            Message.success("删除成功！");
          }
        }).catch(()=>{
          Message.error("删除出错！");
        })
      }).catch(()=>{
        
      })
    },
    setServer(id){
      if(id!=null){
        this.visable=true;
        var object=this.data.find(i=>i.id==id);
        this.server={
          enable:false,
          hostname: object.hostname,
          id: id,
          port: object.port,
          registerEnable: null,
          rootPassword: null,
          fileKey: null,
          sqlkey: null,
          disk: object.disk,
          status: object.status,
        }
      }else{
        this.server={
          hostname: '',
          port: '',
          id:null,
          registerEnable: null,
          rootPassword: null,
          fileKey: null,
          sqlkey: null,
          disk: '',
          status: 0,
        }
      }
    },
    editData(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true;
          if(this.server.enable==false){
            this.server.rootPassword=null
            this.server.sqlkey=null
            this.server.fileKey=null
          }else{
            if(this.server.status==1)
              this.server.rootPassword=null
              this.server.fileKey=null
            if(this.server.status==0)
              this.server.sqlkey=null
              this.server.fileKey=null
            if(this.server.status==2){
              this.server.sqlkey=null
              this.server.rootPassword=null
            }
          }
          if(this.server.enable && this.server.status==2){
            this.$refs.upload.submit();
          }else{
            console.log('非文件修改');
            updateServerInfo(this.server).then(response=>{
              if(response.object==false){
                this.server={}
                this.loading=false;
                this.visable=false;
                this.$message.error("连接失败,请重新修改")
                this.getDataInfo();
              }else{
                this.server={}
                this.loading=false;
                this.data = response.object
                this.visable=false;
                this.$message.success("修改成功")
              }
            }).catch(()=>{
              this.server={}
              this.loading=false;
            })
          }
        }
      })
    },
    addData(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) 
        {
          this.loading = true;
          if(this.server.enable==false){
            this.server.rootPassword=null
            this.server.sqlkey=null
          }else{
            if(this.server.status==1)
              this.server.rootPassword=null
              this.server.fileKey=null
            if(this.server.status==0)
              this.server.sqlkey=null
              this.server.fileKey=null
            if(this.server.status==2){
              this.server.sqlkey=null
              this.server.rootPassword=null
            }
          }
          if(this.server.status==2){
            this.$refs.uploadADD.submit();
          }else{
            console.log('非文件上传')
            addServerInfo(this.server).then(response=>{
              this.addenable=false;
              this.loading=false;
              if(response.object==false){
                this.getDataInfo()
                this.$message.error("添加失败!连接不成功！")
              }else{
                console.log(response)
                this.getDataInfo()
                this.$message.success("添加成功")
              }
            }).catch(()=>{
              this.loading=false;
            })
          }
        }
      })
    },
    onSuccess(response){
      this.server.fileKey=response.object
      if(this.server.id!=null){
        updateServerInfo(this.server).then(response=>{
          this.server={}
          this.loading=false;
          this.visable=false;
          if(response.object==false){
            this.$message.error("连接失败,请重新修改")
            this.getDataInfo();
          }else{
            this.data = response.object
            this.$message.success("修改成功")
          }
        }).catch(()=>{
          this.server={}
          this.loading=false;
        })
      }else{
        addServerInfo(this.server).then(response=>{
            this.addenable=false;
            this.loading=false;
            if(response.object==false){
              this.getDataInfo()
              this.$message.error("添加失败!连接不成功！")
            }else{
              console.log(response)
              this.getDataInfo()
              this.$message.success("添加成功")
            }
          }).catch(()=>{
            this.loading=false;
          })
      }
    },
    onError(response){
      this.$message.error(response.object)
    }
  }
}
</script>

<style>
  .time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }

 .panel-group {
  margin-top: 18px;
 }

  .card-panel-col {
    margin-bottom: 32px;
  }
  
</style>