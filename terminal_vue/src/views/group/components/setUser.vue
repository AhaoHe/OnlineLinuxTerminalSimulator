<template>
  <el-transfer class="parent"
    :props="{
      key: 'uid',
      label: 'username'
    }"
    @change="handleChange"
    :titles="title"
    v-model="value"
    @left-check-change="getKey"
    :data="data">
     <span slot-scope="{ option }" :style="`color:${option.server_status==1?'green':'red'}`">{{ option.username }}-{{ option.role_name }}-{{ option.gname }}</span>
     <el-button class="transfer-footer" v-if="query.level!=10" :disabled="checkedKey.length==0" :loading="loading" type="danger" slot="left-footer" size="small" @click="rejectApply">拒绝申请</el-button>
  </el-transfer>
</template>

<script>
import { UnChecked,Checked,AddTo,RemoveFrom,ApplyUnChecked,delApply } from '@/api/group'
import store from '@/store'
export default {
    props:{
        setForm: {
            type: Object,
            default: {}
        }
    },
    mounted(){
        //this.getUnCheckedData(this.query)
        this.title = store.getters.level==10?['其他组成员', '组成员']:['申请人员','组成员']
        this.getData(this.query)
    },
    data() {
        return {
            query:{
                search:'',
                level:this.$store.getters.level,
                groupId:this.setForm.groupId,
                serverId:this.setForm.serverId,
                name:this.setForm.name,
            },
            data: [],
            value: [],
            title:['其他组成员', '组成员'],
            checkedKey:[],
            loading:false
        };
    },
    methods:{
        getData(query){
            new Promise((resolve,reject)=>{
                if(this.query.level==10){
                    UnChecked(query).then(r=>{
                        console.log(r.object)
                        this.data=r.object
                        resolve()
                    }).catch()
                }else{
                    ApplyUnChecked(this.query.groupId,this.query.search).then(r=>{
                        console.log(r.object)
                        this.data=r.object
                        resolve()
                    }).catch()
                }
            }).then(()=>{
                Checked(query).then(r=>{
                    console.log(r.object);
                    this.data=this.data.concat(r.object)
                    r.object.forEach(element => {
                        if(this.query.level!=10)
                            element.disabled=(element.level>=this.$store.getters.level);
                        this.value.push(element.uid)
                    });
                }).catch
            }).catch
        },
        filterMethod(query, item) {
        //   return item.username.indexOf(query) > -1;
            getAdmin(query).then(r=>{
                this.data=r.object
            }).catch()
        },
        handleChange(value, direction, movedKeys) {
            //console.log(value, direction, movedKeys);
             //可以通过direction回调right/left 来进行操作，right：把数字移到右边，left把数据移到左边
             if(direction === "right") {//添加组
                this.$confirm(`是否移入该组?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    let val = this.data.filter(e=>{return movedKeys.indexOf(e.uid)!=-1})
                    AddTo(val,this.setForm.groupId).then(r=>{
                        if(r.object){
                            this.data.forEach(e=>{
                                if(movedKeys.indexOf(e.uid)!=-1)
                                    e.gname=this.query.name;
                            })
                            this.$message.success("添加成功!")
                        }
                        else{
                            this.value=this.value.filter(e=>{return movedKeys.indexOf(e)==-1})
                            this.$message.success("添加失败!")
                        }
                    }).catch(()=>{
                        this.value=this.value.filter(e=>{return movedKeys.indexOf(e)==-1})
                    })
                }).catch()
             }
             if(direction === "left") {//移出组
                this.$confirm(`是否移出该组?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    let val = this.data.filter(e=>{return movedKeys.indexOf(e.uid)!=-1})
                    RemoveFrom(val,this.query.groupId).then(r=>{
                        if(r.object){
                            this.data.forEach(e=>{
                                if(movedKeys.indexOf(e.uid)!=-1)
                                    e.gname='无组';
                            })
                            this.$message.success("添加成功!")
                        }
                        else{
                            this.value=this.value.concat(movedKeys)
                            this.$message.error("添加失败!可能原因：无组无法移除")
                        }
                    }).catch(()=>{
                        this.value=this.value.concat(movedKeys)
                    })
                }).catch()
             }   
        },
        getKey(key){
            this.checkedKey=[]
            this.data.forEach(e=>{
                if(key.indexOf(e.uid)!=-1){
                    this.checkedKey.push(e.username)
                }
            })
        },
        rejectApply(){
            this.$confirm(`是否拒绝申请?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(()=>{
                console.log(this.checkedKey)
                this.loading=true
                delApply(this.query.groupId,this.checkedKey.join(',')).then(r=>{
                    this.getData(this.query)
                    this.loading=false
                    this.$message.success('已撤销申请')
                }).catch()
            }).catch()
        }
    },
    watch:{
        'setForm.groupId':function(newVal){
            this.query.groupId=this.setForm.groupId
            this.query.serverId=this.setForm.serverId
            this.query.name=this.setForm.name
            this.value=[]
            this.getData(this.query)
        },
        /* 'value':function(newVal){
            console.log(newVal)
        } */
    }
};
</script>
<style scoped>
.parent /deep/ .el-transfer-panel{
    width:240px;
    height: 340px;
}
.transfer-footer{
    margin-left: 74px;
}
</style>