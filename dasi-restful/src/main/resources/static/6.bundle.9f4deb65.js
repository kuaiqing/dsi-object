(window.webpackJsonp=window.webpackJsonp||[]).push([[6],{473:function(t,e,a){"use strict";var n=a(484),i=a.n(n),s=a(146);i.a.defaults.withCredentials=!0;var o=i.a.create({timeout:6e4});o.interceptors.request.use((function(t){if(t.params)for(var e in t.params)"string"==typeof t.params[e]&&(t.params[e]=encodeURI(t.params[e]));return t}),(function(t){return Object(s.Message)({message:t.message,type:"error"}),Promise.reject()})),o.interceptors.response.use((function(t){if(200===t.status)return(t.data.status&&500==t.data.status||!t.data.success)&&Object(s.Message)({message:t.data.message,type:"error"}),t.data.status&&234==t.data.status&&(window.location.href="http://"+window.location.host+"/#/login"),t.data}),(function(t){return Object(s.Message)({message:t.message,type:"error"}),Promise.reject()}));var r=o,l="http://172.16.132.48:8088/dsi",c={};function u(t,e){t.save=function(t){return r({url:l+"/info/".concat(e,"/save"),method:"post",data:t})},t.list=function(){return r({url:l+"/info/".concat(e,"/list"),method:"get"})},t.delete=function(t){return r({url:l+"/info/".concat(e,"/delete/").concat(t),method:"post"})},t.batchDelete=function(t){return r({url:l+"/info/".concat(e,"/batch/delete"),method:"post",data:t})},t.copy=function(t){return r({url:l+"/info/".concat(e,"/copy"),method:"post",data:t})}}c.group={},u(c.group,"group"),c.group.getTargetList=function(t){return r({url:l+"/info/group/target",method:"get",params:t})},c.target={},u(c.target,"target"),c.target.getPointList=function(t){return r({url:l+"/info/target/point",method:"get",params:t})},c.point={},u(c.point,"point"),c.pointScheduler={},c.pointScheduler.include=function(t){return r({url:l+"/point/scheduler/include",method:"get",params:t})},c.pointScheduler.exclude=function(t){return r({url:l+"/point/scheduler/exclude",method:"get",params:t})},c.pointScheduler.batchSave=function(t){return r({url:l+"/point/scheduler/batch/save",method:"post",data:t})},c.pointScheduler.delete=function(t){return r({url:l+"/point/scheduler/delete",method:"post",data:t})},c.pointScheduler.batchDelete=function(t){return r({url:l+"/point/scheduler/batch/delete",method:"post",data:t})},c.handler={},c.handler.list=function(){return r({url:l+"/info/handler/list",method:"get"})},c.scheduler={},u(c.scheduler,"scheduler"),c.scheduler.getPointList=function(t){return r({url:l+"/info/scheduler/point",method:"get",params:t})},c.getAllStatus=function(){return r({url:l+"/result/summary",method:"get"})},c.getResultList=function(t){return r({url:l+"/result/list",method:"get",params:t})},c.getAlarmsCount=function(){return r({url:l+"/result/alert",method:"get"})},c.exportExcel=function(){return l+"/result/excel"},c.getHistoryDiskfree=function(t){return r({url:l+"/result/history/disk/free",method:"get",params:t})},c.getHistoryHttpService=function(t){return r({url:l+"/result/history/http/service",method:"get",params:t})},c.getHistoryProcessStatus=function(t){return r({url:l+"/result/history/process/status",method:"get",params:t})},c.getHistoryRecordNumber=function(t){return r({url:l+"/result/history/record/number",method:"get",params:t})};e.a=c},489:function(t,e,a){},492:function(t,e,a){t.exports=a.p+"img/image/root.7a61993.png"},493:function(t,e,a){t.exports=a.p+"img/image/group.d33328a.png"},494:function(t,e,a){t.exports=a.p+"img/image/groupSelected.8576644.png"},514:function(t,e,a){"use strict";var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{height:"100%",width:"100%"}},[a("el-tree",{ref:"treecomponent",attrs:{data:t.targetData,props:t.defaultProps,"node-key":"id","default-expand-all":"","expand-on-click-node":!1,"highlight-current":!0},on:{"node-click":t.handleTreeNodeClick},scopedSlots:t._u([{key:"default",fn:function(e){var n=e.node,i=e.data;return a("span",{staticClass:"custom-node"},[a("span",[a("img",{directives:[{name:"show",rawName:"v-show",value:!i.id,expression:"!data.id"}],staticStyle:{"vertical-align":"middle"},attrs:{src:t.root,width:"18",height:"18"}}),t._v(" "),a("img",{directives:[{name:"show",rawName:"v-show",value:i.id&&i.targetType&&!n.isCurrent,expression:"data.id && data.targetType && !node.isCurrent"}],staticStyle:{"vertical-align":"middle"},attrs:{src:t.group,width:"18",height:"18"}}),t._v(" "),a("img",{directives:[{name:"show",rawName:"v-show",value:i.id&&!i.targetType,expression:"data.id && !data.targetType"}],staticStyle:{"vertical-align":"middle"},attrs:{src:t.net,width:"18",height:"18"}}),t._v(" "),a("img",{directives:[{name:"show",rawName:"v-show",value:i.id&&i.targetType&&n.isCurrent,expression:"data.id && data.targetType && node.isCurrent"}],staticStyle:{"vertical-align":"middle"},attrs:{src:t.groupSelected,width:"18",height:"18"}}),t._v(" "),a("span",{staticStyle:{"vertical-align":"middle","margin-left":"2px"}},[t._v(t._s(i.code?n.label+"["+i.code+"]":n.label))])])])}}])})],1)};n._withStripped=!0;var i=a(473),s={name:"home",props:{handleNodeClick:{type:Function,default:null}},data:function(){return{defaultProps:{children:"children",label:"name"},root:a(492),group:a(493),groupSelected:a(494),net:a(549),targetData:[{name:"巡检对象分组",children:[],id:"-1"}]}},mounted:function(){this.getGroupList(),this.handleNodeClick({id:"-1"})},computed:{},methods:{setCurrentNode:function(t){this.$refs.treecomponent.setCurrentKey(t)},getTargetList:function(){var t=this;i.a.group.getTargetList({page:0,size:2e4}).then((function(e){if(e.success){var a=e.data.content,n=t.targetData[0].children;for(var i in a){var s=a[i];for(var o in n){var r=n[o];if(s.group.id==r.id){r.children.push(s);break}}}}}))},getGroupList:function(){var t=this;i.a.group.list().then((function(e){if(e.success){for(var a in e.data)e.data[a].children=[];t.targetData[0].children=e.data,t.getTargetList()}}))},handleTreeNodeClick:function(t,e){this.handleNodeClick&&this.handleNodeClick(t)}}},o=(a(550),a(145)),r=Object(o.a)(s,n,[],!1,null,"1c1ddbfe",null);r.options.__file="src/views/checkpoint/component/targettree.vue";e.a=r.exports},549:function(t,e,a){t.exports=a.p+"img/image/net.63e0b67.png"},550:function(t,e,a){"use strict";var n=a(489);a.n(n).a},551:function(t,e,a){},552:function(t,e,a){},553:function(t,e,a){},784:function(t,e,a){"use strict";var n=a(551);a.n(n).a},785:function(t,e,a){"use strict";var n=a(552);a.n(n).a},786:function(t,e,a){"use strict";var n=a(553);a.n(n).a},805:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"container"},[a("el-row",{attrs:{gutter:5}},[a("el-col",{attrs:{span:4}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"cardHeader"},[a("span",[t._v("巡检对象")])]),t._v(" "),a("div",{staticClass:"cardBody"},[a("TargetTree",{attrs:{handleNodeClick:t.handleNodeClick}})],1)])],1),t._v(" "),a("el-col",{attrs:{span:20}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"cardHeader"},[a("el-row",[a("el-col",{attrs:{span:12}},[a("el-button",{staticClass:"vbi-button",attrs:{type:"primary",size:"small",icon:"el-icon-plus"},on:{click:t.addPoint}},[t._v("新增")]),t._v(" "),a("el-button",{staticClass:"vbi-button",attrs:{type:"primary",size:"small",icon:"el-icon-edit"},on:{click:t.copyPoint}},[t._v("复制")]),t._v(" "),a("el-button",{staticClass:"vbi-button",attrs:{type:"danger",size:"small",icon:"el-icon-delete"},on:{click:t.batchDeletePoint}},[t._v("删除")])],1),t._v(" "),a("el-col",{staticStyle:{"text-align":"right","padding-right":"10px"},attrs:{span:12}},[a("el-input",{staticStyle:{width:"150px"},attrs:{size:"small",placeholder:"项目名称",clearable:""},model:{value:t.searchData.pointName,callback:function(e){t.$set(t.searchData,"pointName",e)},expression:"searchData.pointName"}}),t._v(" "),a("el-button",{staticClass:"vbi-button",attrs:{type:"primary",size:"small",icon:"el-icon-search",plain:""},on:{click:t.onSearch}},[t._v("查询")])],1)],1)],1),t._v(" "),a("div",{staticClass:"cardBody"},[a("el-table",{attrs:{height:t.tableMaxHeight,data:t.pointList,border:"",stripe:!0},on:{"selection-change":t.handleSelectionChange}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),t._v(" "),a("el-table-column",{attrs:{label:"项目名称",prop:"name"}}),t._v(" "),a("el-table-column",{attrs:{label:"巡检对象",prop:"target.name"}}),t._v(" "),a("el-table-column",{attrs:{label:"项目类型",prop:"handler.name"}}),t._v(" "),a("el-table-column",{attrs:{label:"超时时间",prop:"executorTimeout",width:"80"}}),t._v(" "),a("el-table-column",{attrs:{label:"是否禁用",width:"80"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-switch",{attrs:{value:e.row.disable,"active-color":"#ff4949","inactive-color":"#13ce66"},on:{change:function(a){return t.handleChangeStatus(a,e.row)}}})]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"操作",align:"center",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{staticClass:"linkButton",attrs:{size:"mini",type:"text"},on:{click:function(a){return t.handleEditTarget(e.row)}}},[t._v("修改")]),t._v(" "),a("el-button",{staticClass:"linkButton danger",attrs:{size:"mini",type:"text"},on:{click:function(a){return t.handleDeleteTarget(e.row)}}},[t._v("删除")])]}}])})],1),t._v(" "),a("div",{staticStyle:{float:"right"}},[a("el-pagination",{attrs:{"current-page":t.pageInfo.page,"page-size":t.pageInfo.size,layout:"prev, pager, next,total",total:t.pageInfo.total},on:{"current-change":t.handleCurrentChange,"update:currentPage":function(e){return t.$set(t.pageInfo,"page",e)},"update:current-page":function(e){return t.$set(t.pageInfo,"page",e)}}})],1)],1)])],1)],1),t._v(" "),a("EditPoint",{attrs:{visible:t.dlgEditPoint,pointData:t.selectPointData,handlerList:t.handlerList},on:{close:function(e){t.dlgEditPoint=!1},save:t.handleSavePoint}}),t._v(" "),a("CopyPoint",{attrs:{visible:t.dlgCopyPoint,pointDatas:t.multipleSelection},on:{close:function(e){t.dlgCopyPoint=!1},save:t.handleCopyPointSave}})],1)};n._withStripped=!0;var i=a(514),s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{staticClass:"vbi-form",attrs:{title:t.title,"close-on-click-modal":!1,visible:t.visible,width:"750px"},on:{open:t.initialize,close:t.handleClose}},[a("el-tabs",{attrs:{type:"card"},model:{value:t.activeName,callback:function(e){t.activeName=e},expression:"activeName"}},[a("el-tab-pane",{attrs:{label:"基本信息",name:"first"}},[a("el-form",{ref:"form",attrs:{model:t.pointData,"label-width":"120px",rules:t.rules}},[a("el-form-item",{attrs:{label:"名称",prop:"name"}},[a("el-input",{model:{value:t.pointData.name,callback:function(e){t.$set(t.pointData,"name",e)},expression:"pointData.name"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"类型"}},[a("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"请选择类型"},model:{value:t.pointData.handler.id,callback:function(e){t.$set(t.pointData.handler,"id",e)},expression:"pointData.handler.id"}},t._l(t.handlerList,(function(t){return a("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})})),1)],1),t._v(" "),a("el-form-item",{attrs:{label:"超时时间(秒)"}},[a("el-input-number",{staticClass:"number",attrs:{controls:!1,step:1,precision:0,min:1,max:1e3,label:"超时时间"},model:{value:t.pointData.executorTimeout,callback:function(e){t.$set(t.pointData,"executorTimeout",e)},expression:"pointData.executorTimeout"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"保存周期(小时)"}},[a("el-input-number",{staticClass:"number",attrs:{controls:!1,step:1,precision:0,min:1,max:100},model:{value:t.pointData.appliedPeriod,callback:function(e){t.$set(t.pointData,"appliedPeriod",e)},expression:"pointData.appliedPeriod"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"disable"}},[a("el-switch",{attrs:{"active-color":"#ff4949","inactive-color":"#13ce66","inactive-text":"启用","active-text":"禁用"},model:{value:t.pointData.disable,callback:function(e){t.$set(t.pointData,"disable",e)},expression:"pointData.disable"}})],1)],1)],1),t._v(" "),a("el-tab-pane",{attrs:{label:"检查参数",name:"second"}},[a(t.componentsName,{ref:"params",tag:"component",attrs:{pointData:t.pointData}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:t.handleClose}},[t._v("取 消")]),t._v(" "),a("el-button",{attrs:{loading:t.beginSave,type:"primary"},on:{click:t.handleSavePoint}},[t._v("确 定")])],1)],1)};s._withStripped=!0;a(206);var o=a(473),r={name:"editPoint",components:{diskfree:function(){return a.e(9).then(a.bind(null,809))},processstatus:function(){return a.e(10).then(a.bind(null,810))},recordnumber:function(){return a.e(11).then(a.bind(null,811))},httpservice:function(){return a.e(13).then(a.bind(null,812))}},props:{visible:{type:Boolean,default:!1},pointData:{type:Object,default:{}},handlerList:{type:Array,default:[]}},data:function(){return{title:"新增巡检项目",beginSave:!1,activeName:"first",rules:{name:{required:!0,message:"请输入对象名称",trigger:"blur"}}}},computed:{componentsName:function(){var t=this.pointData.handler.id;return"server_disk_free"==t?"diskfree":"server_process_status"==t?"processstatus":"db_record_number"==t?"recordnumber":"http_service"==t?"httpservice":"diskfree"}},methods:{initialize:function(){this.pointData.id?this.title="修改巡检项目:"+this.pointData.name:this.title="新增巡检项目",this.$refs.params&&this.$refs.params.setParams(),console.log(this.handlerList)},handleSavePoint:function(){var t=this;this.beginSave||(this.beginSave=!0,this.$refs.params.getParams()?this.$refs.form.validate((function(e){e?o.a.point.save(t.pointData).then((function(e){t.beginSave=!1,e.success&&(t.pointData.id?t.$message({message:"成功修改巡检项目！",type:"success"}):t.$message({message:"成功新增巡检项目！",type:"success"}),t.$emit("save"))})):t.beginSave=!1})):this.beginSave=!1)},handleClose:function(){this.$emit("close")}}},l=(a(784),a(145)),c=Object(l.a)(r,s,[],!1,null,"24023647",null);c.options.__file="src/views/checkpoint/component/editpoint.vue";var u=c.exports,d=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{staticClass:"vbi-form",attrs:{title:"复制项目","close-on-click-modal":!1,visible:t.visible,width:"600px"},on:{open:t.initialize,close:t.handleClose}},[a("el-row",{attrs:{gutter:5}},[a("el-col",{attrs:{span:10}},[a("el-card",[a("div",{attrs:{slot:"header"},slot:"header"},[a("span",[t._v("选择目标对象")])]),t._v(" "),a("div",{staticClass:"treeDiv"},[a("TargetTree",{attrs:{handleNodeClick:t.handleNodeClick}})],1)])],1),t._v(" "),a("el-col",{attrs:{span:14}},[a("el-card",[a("div",{attrs:{slot:"header"},slot:"header"},[a("span",[t._v("修改属性：名称必须唯一")])]),t._v(" "),a("el-table",{attrs:{height:600,data:t.pointDatas,border:"",stripe:!0}},[a("el-table-column",{attrs:{label:"名称"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-input",{model:{value:e.row.name,callback:function(a){t.$set(e.row,"name",a)},expression:"scope.row.name"}})]}}])})],1)],1)],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:t.handleClose}},[t._v("取 消")]),t._v(" "),a("el-button",{attrs:{loading:t.beginSave,type:"primary"},on:{click:t.handleSavePoint}},[t._v("确 定")])],1)],1)};d._withStripped=!0;a(209);var p={name:"CopyPoint",components:{TargetTree:i.a},props:{visible:{type:Boolean,default:!1},pointDatas:{type:Array,default:[]}},data:function(){return{beginSave:!1,target:{id:"",name:""}}},methods:{initialize:function(){console.log("open")},handleNodeClick:function(t){t.targetType?(this.target.id=t.id,this.target.name=t.name):(this.target.id="",this.target.name="")},handleSavePoint:function(){var t=this;this.beginSave||(this.target.id?(this.beginSave=!0,this.pointDatas.forEach((function(e){return e.target=t.target})),o.a.point.copy(this.pointDatas).then((function(e){t.beginSave=!1,e.success&&(t.$message({message:"成功复制巡检项目！",type:"success"}),t.$emit("save"),t.handleClose())})).catch((function(){t.beginSave=!1}))):this.$message({message:"请选择目标对象！",type:"warning"}))},handleClose:function(){this.$emit("close")}}},h=(a(785),Object(l.a)(p,d,[],!1,null,"2a7f05c8",null));h.options.__file="src/views/checkpoint/component/copyPoint.vue";var g=h.exports,m={name:"home",components:{TargetTree:i.a,EditPoint:u,CopyPoint:g},data:function(){return{pointList:[],selectTarget:{id:"",name:""},searchData:{pointName:""},pageInfo:{page:1,size:15,total:0},dlgEditPoint:!1,selectPointData:{name:"",target:{id:""},handler:{id:""},executorParams:"{}",executorTimeout:3,appliedPeriod:1,disable:!1},allHandlerList:[],handlerList:[],multipleSelection:[],dlgCopyPoint:!1}},created:function(){var t=this;o.a.handler.list().then((function(e){e.success&&(t.allHandlerList=e.data)}))},computed:{tableMaxHeight:function(){return window.innerHeight-180+"px"}},methods:{addPoint:function(){if(this.selectTarget.id){for(var t in this.handlerList=[],this.allHandlerList)this.allHandlerList[t].targetType==this.selectTarget.targetType&&this.handlerList.push(this.allHandlerList[t]);this.selectPointData={name:"",target:{id:this.selectTarget.id},handler:{id:this.handlerList[0].id},executorParams:"{}",executorTimeout:30,appliedPeriod:1,disable:!1},this.dlgEditPoint=!0}else this.$message({message:"必须选择巡检对象，才能新增巡检项目！",type:"warning"})},copyPoint:function(){!this.multipleSelection||this.multipleSelection.length<1?this.$message({message:"请选择要复制的项目！",type:"warning"}):this.dlgCopyPoint=!0},batchDeletePoint:function(){var t=this;!this.multipleSelection||this.multipleSelection.length<1?this.$message({message:"请选择要删除的项目！",type:"warning"}):this.$confirm("确定要删除所选项目?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){var e=t.multipleSelection.map((function(t){return t.id}));o.a.point.batchDelete(e).then((function(e){e.success&&(t.$message({message:"成功删除所选项目！",type:"success"}),t.getPointList())}))}))},handleCopyPointSave:function(){console.log("CopyPointSave")},handleSelectionChange:function(t){this.multipleSelection=t},handleNodeClick:function(t){this.selectTarget=t,this.getPointList()},getPointList:function(){var t=this,e={page:this.pageInfo.page-1,size:this.pageInfo.size};this.selectTarget.id&&(e.id=this.selectTarget.id),this.searchData.pointName&&(e.name=this.searchData.pointName),o.a.target.getPointList(e).then((function(e){e.success&&(t.pointList=e.data.content,t.pageInfo.total=e.data.totalElements)}))},handleCurrentChange:function(t){this.pageInfo.page=t,this.getPointList()},onSearch:function(){var t=this,e={page:0,size:this.pageInfo.size};this.searchData.pointName&&(e.name=this.searchData.pointName),o.a.target.getPointList(e).then((function(e){e.success&&(t.pointList=e.data.content,t.pageInfo.total=e.data.totalElements)}))},getTypeName:function(t){return"Server"==t?"服务器":"Db"==t?"数据库":"Http"==t?"HTTP服务":""},handleSavePoint:function(){this.dlgEditPoint=!1,this.getPointList()},handleEditTarget:function(t){for(var e in this.handlerList=[],this.allHandlerList)this.allHandlerList[e].targetType==this.selectTarget.targetType&&this.handlerList.push(this.allHandlerList[e]);this.dlgEditPoint=!0,this.selectPointData=JSON.parse(JSON.stringify(t))},handleDeleteTarget:function(t){var e=this;this.$confirm("确定要删除该巡检项目信息?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){o.a.point.delete(t.id).then((function(t){t.success&&(e.$message({message:"成功删除巡检项目！",type:"success"}),e.getPointList())}))}))},handleChangeStatus:function(t,e){var a=this;e.disable=t,o.a.point.save(e).then((function(t){t.success&&a.$message({message:"成功修改巡检项目！",type:"success"})}))}}},f=(a(786),Object(l.a)(m,n,[],!1,null,"e13047b6",null));f.options.__file="src/views/checkpoint/index.vue";e.default=f.exports}}]);