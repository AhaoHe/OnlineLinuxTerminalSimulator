<template>
<div>
  <div >
    <el-button size="small" type="primary" @click="visable=true" style="margin-right:10px">新增用户</el-button>
    <el-popconfirm style="margin-right:10px"
      title="确定禁用这些用户吗？"
      @onConfirm="updateStates"
    >
      <el-button size="small" type="danger" slot="reference">批量黑名单</el-button>
    </el-popconfirm>
    <el-popconfirm
      title="确定删除这些用户吗？"
      @onConfirm="deleteUsers"
    >
      <el-button size="small" type="danger" slot="reference">批量注销</el-button>
    </el-popconfirm>
    <el-button size="small" style="float:right;margin-right:10px" @click="clearFilter">清除过滤器</el-button>
    <div class="block"  style="margin-right:10px;float:right">
      <span class="demonstration" style="margin-right:10px"></span>
      <el-date-picker
        size="small"
        v-model="dataTime"
        type="datetimerange"
        align="right"
        value-format="yyyy-MM-dd HH:mm:ss"
        unlink-panels
        range-separator="至"
        start-placeholder="开始日期"
        @change="selectTime"
        end-placeholder="结束日期"
        :default-time="['09:30:00', '18:00:00']">
      </el-date-picker>
    </div>
  </div>
  <el-card style="margin-top:10px;height:440px;">
    <el-table
      ref="filterTable"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="sortChange"
      @filter-change="filterChange"
      style="width: 100%;">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="ID">
              <span>{{ props.row.uid }}</span>
            </el-form-item>
            <el-form-item label="性别">
              <span>{{ props.row.sex }}</span>
            </el-form-item>
            <el-form-item label="邮箱">
              <span>{{ props.row.mail }}</span>
            </el-form-item>
            <el-form-item label="手机">
              <span>{{ props.row.tel }}</span>
            </el-form-item>
            <el-form-item label="昵称">
              <span>{{ props.row.nickname }}</span>
            </el-form-item>
            <el-form-item label="创建时间">
              <span>{{ props.row.createTime }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        prop="username"
        label="用户名"
        width="200%">
      </el-table-column>
      <el-table-column
        prop="roleName"
        label="角色"
        width="150%"
        :filters="roleFilter"
        column-key="role"
        filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-popover
          placement="right"
          width="200"
          trigger="click">
          <el-select v-model="selectVal" placeholder="请选择" @change="currentSel(scope.row.uid,selectVal,scope.row.groupId)">
            <el-option
              v-for="item in roleFilter"
              :key="item.value"
              :label="item.text"
              :value="item.value">
            </el-option>
          </el-select>
          <el-tag size="mini"
            slot="reference"
            @click="setSelectVal(scope.row.roleId)"
            :type="scope.row.level > 8 ? 'primary' : 'success'">{{scope.row.roleName}}</el-tag>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        prop="lastTime"
        sortable='custom'
        label="最后登录时间"
        :filter-multiple="false"
        :filters="dateFilter"
        column-key = 'datetime'
        width="250%">
      </el-table-column>
      <el-table-column
        prop="state"
        label="状态"
        width="100%"
        :filter-multiple="false"
        :filters="banFilter"
        column-key = 'ban'>
        <template slot-scope="scope">
              <el-switch
              v-model="scope.row.state"
              :active-value="3"
              :inactive-value="0"
              :active-text="scope.row.state==3?'激活':'禁用'"
              @change="changeState(scope)"
              active-color="#13ce66"
              inactive-color="#ff4949">
              </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        width="220%">
        <template slot="header" slot-scope="">
          <el-input
            v-model="data.search"
            size="mini"
            placeholder="输入用户名搜索"/>
        </template>
        <template slot-scope="scope">
          <el-popover style="margin-right:10px"
            placement="left-start"
            :title="`所属组：${scope.row.gname}`"
            width="220"
            trigger="hover"
            :content="`服务器ID：${scope.row.serverId}-------------服务器：${scope.row.server}  服务器用户：${scope.row.serverUsername}`">
            <el-button size="mini" slot="reference">查看</el-button>
          </el-popover>
          <!-- <el-button @click="handleClick(scope.row)" size="mini">查看</el-button> -->
          <el-button type="info" @click="editU(scope.row)" size="mini">编辑</el-button>
          <el-popconfirm style="margin-left:10px"
            placement="top"
            :title="`确定删除${scope.row.username}吗？`"
            @onConfirm="deleteUser(scope.row.username)"
          >
            <el-button slot="reference" type="danger" size="mini">删除</el-button>
          </el-popconfirm>
          <!-- <el-button type="danger" @click="deleteUser(scope.row.username)" size="mini">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <div class="block" style="text-align: center;">
    <el-pagination
      @current-change="getUserBy"
      :current-page.sync="data.current"
      :page-size="data.pageSize"
      layout="total, prev, pager, next, jumper"
      :total="data.total">
    </el-pagination>
  </div>

  <!-- 新增 -->
  <el-dialog title="添加用户" :visible.sync="visable" width="400px">
    <insert-user ref="insertUser"></insert-user>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visable = false">取 消</el-button>
      <el-button @click="reset('insert')">重 置</el-button>
      <el-button :loading="loading" type="primary" @click="add">添 加</el-button>
    </div>
  </el-dialog>
  <!-- 编辑 -->
  <el-dialog title="编辑用户" :visible.sync="evisable" width="400px">
    <edit-user ref="edit" v-if="evisable" :edit-form="editForm"></edit-user>
    <div slot="footer" class="dialog-footer">
      <el-button @click="evisable = false">取 消</el-button>
      <el-button @click="reset('edit')">重 置</el-button>
      <el-button :loading="loading" type="primary" @click="edit">修 改</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
  import { findAllUser , findUserBy , updateState , updateStates , changeUserRole } from '@/api/user'
  import { Message } from 'element-ui';
  import insertUser from './components/insertUser';
  import editUser from './components/editUser';
  export default {
    mounted(){
      this.getUserInfo(1,this.data.pageSize);
    },
    methods: {
      /* filterRole(value, row) {
        //return row.roleId === value;
      }, */
      filterChange(filters){
        if(filters['role']){
          this.data.roleId = filters['role'].join(',')
        }else if(filters['datetime']){
          this.data.dateTime = filters['datetime'][0]
        }else if(filters['ban']){
          console.log(filters['ban'])
          this.data.ban = filters['ban'][0];
        }
        this.getUserBy()
        console.log(this.data)
      },
      handleSelectionChange(val) {
        let select = [];
        for(let i = 0;i<val.length;i++){
          select.push(val[i].username);
        }
        this.multipleSelection = select;
        console.log(select);
      },
      clearFilter() {
        this.$refs.filterTable.clearFilter();
        this.$refs.filterTable.clearSort();
        this.dataTime=[]
        this.data={
          search:'',
          roleId:'',
          dateTime:0,
          order:'',
          total: 0,
          current:1,
          pageSize:6,
          selectTime:'',
          ban:0,
        }
        this.getUserInfo(1,this.data.pageSize);
      },
      sortChange(column, prop, order){
        if(column.order == 'ascending')
          this.data.order = 'ASC'
        else
          this.data.order = 'DESC'
        this.getUserBy()
      },
      changeState(scope) {
        this.$confirm(`是否确认${scope.row.state==3?'禁用':'激活'}该用户?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          updateState(scope.row.username,scope.row.state).then(response=>{
            Message.success(`${scope.row.state!=3?'禁用':'激活'}成功！`)
          }).catch(()=>{
            Message.error(`${scope.row.state!=3?'禁用':'激活'}出错！`)
          })
        }).catch(()=>{
          scope.row.state = scope.row.state==3?0:3;
        })
      },
      getUserBy(){
          findUserBy(this.data).then(response=>{
            this.tableData = response.object.records;
            this.data.total = response.object.total;
            this.data.pageSize = response.object.size;
            this.data.current = response.object.current;
            this.tableData= response.object.records;
          }).catch(()=>{
            Message.error('查找失败！')
          })
      },
      getUserInfo(current,pageSize){
        findAllUser(current,pageSize).then(response=>{
          this.data.total = response.object.users.total;
          this.data.pageSize = response.object.users.size;
          this.data.current = response.object.users.current;
          this.tableData= response.object.users.records;
          console.log(this.tableData)
          this.roleFilter = response.object.rolefilter;
        }).catch(()=>{
          this.tableData=[];
        })
      },
      add(){
        this.loading = true;
        this.$refs.insertUser.onSubmit('registerForm')
        .then(()=>{
          this.loading= false;
          this.visable=false;
          this.getUserBy();
        }).catch(reject=>{
          this.loading=false;
        });
      },
      reset(type){
        if(type == 'insert')
          this.$refs.insertUser.reset()
        else
          this.$refs.edit.reset()
      },
      deleteUser(username){
        updateState(username,7).then(response=>{
            console.log(response)
            this.getUserBy()
            Message.success('删除成功')
          }).catch(()=>{
            Message.error("删除出错！")
          })
      },
      updateStates(){
          updateStates(this.multipleSelection,0).then(response=>{
            this.tableData.forEach(data => {
              if(this.multipleSelection.indexOf(data.username)!=-1){
                data.state=0
              }
            });
            this.$refs.filterTable.clearSelection()
            this.multipleSelection = []
            console.log(this.multipleSelection)
            Message.success('禁用成功！')
          }).catch(()=>{
            Message.error("禁用出错！")
          })
      },
      deleteUsers(){
          updateStates(this.multipleSelection,7).then(response=>{
            this.getUserBy();
            Message.success('删除成功')
          }).catch(()=>{
            Message.error("删除出错！")
          })
      },
      editU(row){
        switch(row.sex){
          case '男':
            row.sex = 1;
            break;
          case '女':
            row.sex = 0;
            break;
          default:
            row.sex = 2;
            break;
        }
        this.editForm = {
                username: row.username,
                password: false,
                nickname: row.nickname,
                sex: row.sex,
                state: row.state,
                mail:row.mail,
                tel: row.tel
            };
        this.evisable = true;
      },
      edit(){
        this.loading = true;
        this.$refs.edit.onSubmit('registerForm')
        .then(()=>{
          this.loading= false;
          this.evisable=false;
          this.getUserBy();
        }).catch(reject=>{
          this.loading=false;
        });
      },
      selectTime(){
        this.data.selectTime = this.dataTime.join(',')
        this.getUserBy()
      },
      setSelectVal(val){
        this.selectVal=val
      },
      currentSel(uid,roleId,groupId){
        changeUserRole(uid,roleId,groupId).then(r=>{
          this.getUserBy();
          this.$message.success('修改角色成功')
        }).catch()
      }
    },
    data() {
      return {
        visable:false,
        evisable:false,
        editForm:{},
        roleFilter:[],
        dateFilter:[/* {text: '半小时内', value: 30}, {text: '一小时内', value: 60}, {text: '1天内', value: 60*24},{text: '3天内', value: 60*24*3}, {text: '一周前', value: 60*24*7},{text: '一个月前', value: 60*24*30}, */{text: '未登录', value: -1}],
        banFilter:[{text: '黑名单', value: -1},{text: '白名单', value: 1}],
        tableData: [],
        multipleSelection:[],
        loading:false,
        dataTime:[],
        data:{
          search:'',
          roleId:'',
          dateTime:0,
          order:'',
          total: 0,
          current:1,
          pageSize:6,
          selectTime:'',
          ban:0,
        },
        selectVal:'',
      }
    },
    components:{
      insertUser,
      editUser,
    },
    watch: {
      'data.search':function(newVal){
          /* findLikeUser(newVal,this.data.current,this.data.pageSize).then(response=>{
            this.tableData = response.object.records;
            this.data.total = response.object.total;
            this.data.pageSize = response.object.size;
            this.data.current = response.object.current;
            this.tableData= response.object.records;
          }).catch(()=>{
            Message.error('查找失败！')
          }) */
          this.data.search = newVal;
          console.log(this.data)
          findUserBy(this.data).then(response=>{
            this.tableData = response.object.records;
            this.data.total = response.object.total;
            this.data.pageSize = response.object.size;
            this.data.current = response.object.current;
            this.tableData= response.object.records;
          }).catch(()=>{
            Message.error('查找失败！')
          })
      },
    },
  }
</script>
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>