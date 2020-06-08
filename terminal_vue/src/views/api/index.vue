<template>
    <div>
        <el-container >
            <el-aside style="background-color: white">
              <el-card>
                <div slot="header" class="clearfix">
                  <span>API管理</span>
                  <el-button style="float: right;" type="text" @click="addRoot">添加根API</el-button>
                </div>
                <el-tree
                :data="data"
                @node-click="clickNode"
                draggable
                node-key="id"
                :expand-on-click-node="false"
                :allow-drop="allowdrop">
                <div class="custom-tree-node" slot-scope="{ node, data }">
                  <span>{{ node.label }}</span>
                  <span>
                    <el-button
                      type="text"
                      size="mini"
                      v-if="node.level==1"
                      @click="() => append(data)">
                      添加
                    </el-button>
                    <el-button
                      type="text"
                      size="mini"
                      @click="() => remove(node, data)">
                      删除
                    </el-button>
                  </span>
                </div>
                </el-tree>
                <el-divider><i class="el-icon-brush"></i></el-divider>
                <div class="bottom">
                    <el-button type="text" class="button" @click="getDataInfo" style="margin-left:40px">刷新</el-button>
                    <el-button type="text" :disabled="enable" @click="getDataInfo()" class="button">重置</el-button>
                    <el-button type="text" class="button" @click="setData">提交</el-button>
                </div>
              </el-card>
            </el-aside>
            <el-container>
            <el-main>
                <el-card>
                  <data-form :data-form-id="formId" @func="getDataInfo"></data-form>
                </el-card>
            </el-main>
            </el-container>
        </el-container>
    </div>
</template>
<script>
import { getAPILabel,setAPILabel,delAPILabel } from '@/api/api'
import DataForm from './components/form'
export default {
    mounted(){
        this.getDataInfo()
    },
    data() {
      return {
        data: [],
        deldata:[],
        visible: false,
        enable:true,
        index:1,
        formId:0,
      };
    },
    methods:{
        getDataInfo(){
            getAPILabel().then(r=>{
                this.data = r.object.data
                this.index = r.object.maxId
                this.enable=true
            }).catch(()=>{
                this.$message.error('加载菜单失败')
            });
        },
        setData(){
          new Promise((resolve, reject) => {
            console.log(this.deldata)
            //修改
            setAPILabel(this.data).then(r =>{
              resolve()
            }).catch(()=>{reject()})
            //删除
            if(this.deldata.length>0){
              delAPILabel(this.deldata).then(r=>{
                console.log(this.deldata)
                resolve();
              }).catch(()=>{reject()})
            }
          }).then(resolve=>{
            this.enable=false;
              this.deldata=[]
              this.getDataInfo();
              this.formId=0;
              this.$message.success('修改成功!')
          }).catch(reject=>{
              this.enable=false;
              this.deldata=[]
              this.getDataInfo();
          })
        },
        append(data) {
          this.$confirm(`是否添加子菜单?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(()=>{
            const newChild = { id: this.index++, label: '新增子菜单', newData:true,children: [] };
            if (!data.children) {
              this.$set(data, 'children', []);
            }
            data.children.push(newChild);
            this.enable=false;
          }).catch(()=>{})
        },
        addRoot(){
          this.$confirm(`是否添加根菜单?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(()=>{
            const newRoot = { id: this.index++, label: '新增根菜单',newData:true, children: [{
              id: this.index++, label: '新增子菜单', newData:true,children: []
            }] };
            this.data.push(newRoot)
            this.enable=false;
          }).catch(()=>{})
        },
        remove(node, data) {
          this.$confirm(`是否删除该菜单?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(()=>{
            const parent = node.parent;
            const children = parent.data.children || parent.data;
            const index = children.findIndex(d => d.id === data.id);
            this.deldata.push(children[index].id);
            if(children[index].children!=null&&children[index].children.length>0){
              children[index].children.forEach(element => {
                this.deldata.push(element.id);
              });
            }
            children.splice(index, 1);
            this.enable=false;
          }).catch(()=>{})
        },
        allowdrop(draggingNode, dropNode, type){
          this.enable=false;
          console.log(dropNode)
          if(draggingNode.level==1&&dropNode.level>=2){
            return false;
          }
          if((draggingNode.level==2&&dropNode.level>=2&&type=='inner')||(draggingNode.level==2&&dropNode.level==1&&(type=='prev'||type=='next'))){
            return false;
          }
          return true;
        },
        clickNode(data){
            this.formId = data.id==null?0:data.id;
        }
    },
    components:{
      DataForm
    }
}
</script>
<style scoped>
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
</style>