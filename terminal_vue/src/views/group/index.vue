<template>
    <div>
      <el-container>
        <el-main style="width:30%">
          <command :group-id="GId" :g-name="GName" :server-id="serverId"></command>
        </el-main>

        <el-main style="width:70%">
        <div style="margin-bottom:20px;margin-top:20px">组管理
            <el-button v-if="showable" type="primary" size="mini" style="margin-right:10px;float:right" @click="add">新增组</el-button>
        </div>
        <el-card>
            <el-table
            @row-click="getGroupId"
            ref="filterTable"
            :data="data"
            style="width: 100%;">
                <el-table-column
                label="组名">
                  <template slot-scope="scope">
                    {{ scope.row.name }}
                    <el-tag style="margin-left:10px;" v-if="scope.row.status==0?true:false" type="success" size="mini">默认组</el-tag>
                  </template>
                </el-table-column>
                <el-table-column 
                prop="groupName"
                label="系统组名">
                </el-table-column>
                <el-table-column 
                prop="serverId"
                label="所属服务器">
                  <template slot-scope="scope">
                    {{ scope.row.serverId+'号服务器' }}
                  </template>
                </el-table-column>
                <el-table-column
                label="采用方式">
                  <template slot-scope="scope">
                    {{ scope.row.filter==true?'过滤危险命令行':'允许部分命令行' }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="240">
                    <template slot-scope="scope">
                        <el-button type="text" size="mini" style="margin-right:10px;" @click="openadmin=true;setForm={groupId:scope.row.id,serverId:scope.row.serverId,name:scope.row.name};">管理成员</el-button>
                        <el-button type="info" @click="edit(scope.row)" v-if="scope.row.status==0?false:true" size="mini" >编辑</el-button>
                        <el-popconfirm style="margin-left:10px"
                            placement="top"
                            :title="`确定删除${scope.row.name}吗？`"
                            @onConfirm="deleteData(scope.row.id)"
                            v-if="ifshow(scope.row.status)">
                            <el-button slot="reference" type="danger" size="mini" >删除</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <el-dialog :title="addif==true?'新 增':'编 辑'" :visible.sync="visable" width="400px">
            <el-form ref="form" :model="upload" :rules="rules" label-width="120px" class="resiger-box">
            <el-form-item label="ID" v-if="!addif">
                <span v-text="upload.id"></span>
            </el-form-item>
            <el-form-item label="组名" prop="name">
                <el-input type="text" style="width: 80%;" placeholder="组名" v-model="upload.name"/>
            </el-form-item>
            <el-form-item label="系统组名" prop="groupName" >
                <el-input type="text" style="width: 80%;" placeholder="系统组名" v-model="upload.groupName"/>
            </el-form-item>
            <el-form-item label="服务器" v-if="addif" prop="serverId">
                <template>
                  <el-select v-model="upload.serverId" placeholder="请选择">
                    <el-option
                      v-for="item in server"
                      :key="item"
                      :label="item+'号服务器'"
                      :value="item">
                    </el-option>
                  </el-select>
                </template>
            </el-form-item>
            <el-form-item label="命令行安全设置" >
              <el-switch 
              v-model="upload.filter"
              :active-text="upload.filter==true?'过滤危险命令行':'允许部分命令行'"
              style="margin-right:10px"></el-switch>
            </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" placement="left-start">
              <el-button @click="visable = false">取 消</el-button>
              <el-button :loading="loading" type="primary" @click="submit('form')">{{ addif==true?'新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>

      <el-dialog title="设置成员" :visible.sync="openadmin" width="740px">
        <template slot="title">
          <span style="margin-right:10px">设置成员</span>
          <el-tooltip class="item" effect="dark" content="红色为系统封禁用户，绿色为正常用户" placement="top">
            <i class="el-icon-warning-outline"></i>
          </el-tooltip>
        </template>
        <set-user :set-form="setForm"></set-user>
      </el-dialog>
      
      </el-main>
      </el-container>
    </div>
</template>
<script>
import { getGroupList,delGroup,SaveOrUpdate,serverList,validateName } from '@/api/group'
import SetUser from './components/setUser'
import Command from './components/command'
import store from '@/store'
export default {
    mounted(){
        this.getData()
    },
    data(){
        var validateGName = (rules, value, callback) => {
            if(value=='') callback(new Error('不能为空!'))
            var patrn=/[a-zA-Z]$/;  
            if (!patrn.exec(value)){
                callback(new Error('只能输入字母!'));
            }else{
              callback()
            }
        };
        return {
            visable:false,
            openadmin:false,
            data:[],
            upload:{
              id:0,
              name:'',
              groupName:'',
              serverId:'',
              filter: true
            },
            addif:true,
            loading:false,
            rules:{
              name: [
                  {required: true, message: '不可为空', trigger: 'blur'},
              ],
              groupName: [
                  { min: 4, max: 12, message: '长度在 4 到 12 个字符', trigger: 'blur' },
                  {required: true, message: '不可为空', trigger: 'blur'},
                  { validator: validateGName, trigger: 'blur' },
              ],
              serverId: [
                { required: true, message: '请选择服务器', trigger: 'change' }
              ],
            },
            flag:false,
            showable:store.getters.level==10,
            server:[],
            setForm:{},
            GId:0,serverId:0,GName:'无'
        }
    },
    methods:{
      getData(){
        getGroupList(this.$store.getters.level,this.$store.getters.groupId).then(r=>{
          console.log(r.object)
          this.data=r.object
        }).catch()
      },
      add(){
        serverList().then(r=>{
          this.server=r.object
        }).catch();
        this.addif=true;
        this.visable=true;
        this.loading=false;
        this.upload={
            id:-1,
            name:'',
            groupName:'',
            serverId:'',
            filter: true,
            status:3
        };
      },
      edit(object){
        this.addif=false;
        this.visable=true;
        this.loading=false;
        this.upload=object;
      },
      submit(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading=true;
            this.flag=false;
            new Promise((resolve, reject) => {
              if(this.addif){
                validateName(this.upload.groupName,this.upload.serverId).then(response=>{
                  if(response.object){
                    this.flag=true;
                    resolve()
                  }else{
                    this.$message.error("系统组名已经存在");
                    this.loading=false;
                  }
                }).catch()
              }else{
                this.flag=true
                resolve()
              }
            }).then(reslove=>{
              if(this.flag){
                SaveOrUpdate(this.upload).then(r=>{
                  this.loading=false;
                  this.getData()
                  if(r.object){
                    this.visable=false;
                    this.loading=false;
                    this.$message.success((this.upload.id==-1?"新建":"修改")+"成功")
                  }else{
                    this.$message.error((this.upload.id==-1?"无法访问该服务器":"无法访问该服务器"))
                  }
                }).catch()
              }
            }).catch()
          } else {
            return false;
          }
        });
      },
      deleteData(id){
        delGroup(id).then(r=>{
          this.getData()
          this.$message.success("删除成功")
        }).catch()
      },
      getGroupId(row){
          this.GId = row.id
          this.serverId = row.serverId
          this.GName = row.name
      },
      ifshow(status){
        return (status==0?false:true)&&(store.getters.level==10)
      }
    },
    components:{
      SetUser,
      Command
    }
    
}
</script>
<style lang="scss" scoped>
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

  .card-panel-col {
    margin-bottom: 32px;
  }

}
</style>