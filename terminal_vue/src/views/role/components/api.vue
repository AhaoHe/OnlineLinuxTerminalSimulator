<template>
    <div>
        <div v-text="'选中角色:'+roleName" style="margin-bottom:10px;color:Silver;"></div>
        <el-input size="mini" style="margin-bottom:10px"
        placeholder="输入API关键字查询"
        v-model="filterText">
        </el-input>
        <el-tree
        :data="data"
        :show-checkbox="roleId!=0"
        node-key="id"
        ref="tree"
        @check-change="change"
        :props="defaultProps"
        :filter-node-method="filterNode">
        <div class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>{{ data.url }}</span>
          </div>
        </el-tree>
        <el-divider><i class="el-icon-brush"></i></el-divider>
        <el-button type="text" :disabled="roleId==0" @click="getAPI">刷新</el-button>
        <div class="bottom">
            <el-button type="text" @click="addChecked" :disabled="roleId==0">提交</el-button>
        </div>
    </div>
</template>
<script>
import { getAPILabel } from '@/api/api'
import { getRoleAPIChecked,addRoleAPI } from '@/api/role'
export default {
    props: {
        roleName: {
            type: String,
            default: '无'
        },
        roleId: {
          type: Number,
          default: 0
        }
    },
    mounted(){
        this.getAPI()
    },
    data(){
        return{
            filterText: '',
            data:[],
            defaultProps: {
                children: 'children',
                label: 'label'
            }
        }
    },
    methods:{
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        getAPI(){
            getAPILabel().then(r=>{
              this.data=r.object.data
              console.log(this.data)
            }).catch(()=>{})
            if(this.roleId!=0){
              this.getAPIChecked(this.roleId)
              this.$message.success('刷新成功')
            }
        },
        getAPIChecked(val){
          getRoleAPIChecked(val).then(r=>{
            const val = r.object;
            let checked =[];
            val.forEach(element => {
              let index =this.data.findIndex(e=>e.id == element.apiId);
              if(index==-1||this.data[index].url=='/**'){
                checked.push(element.apiId)
              }
            });
            this.$refs.tree.setCheckedKeys(checked);
          }).catch()
        },
        addChecked(){
          let checked = this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys());
          addRoleAPI(checked,this.roleId).then(r=>{
            this.$message.success("修改成功")
          }).catch()
        },
        change(data,enable){
          if(enable&&data.url=='/**'){
            this.$refs.tree.setCheckedKeys([data.id]);
          }
        }
    },
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
      'roleId': function(newVal){
        this.getAPIChecked(newVal);
      }
    },
}
</script>
<style scoped>
  .bottom {
    /* margin-top: 13px;
    line-height: 12px; */
    float: right;
    margin-bottom: 14px;
  }
  .el-divider--horizontal{
     margin: 20px 0;
     background: 0 0;
     border-top: 1px dashed #e8eaec;
  } 
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
</style>