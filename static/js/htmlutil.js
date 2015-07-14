

PageUtil = function(id){
	this.id = id;
	var head = ""; 
	head += "<div class='modal fade' id='"+id+"'>";
	head += "<div class='modal-dialog' id='"+id+"_dialog'>";		
    
    var footer = "";
	footer += "</div>";
    footer +="</div>";
    
     this.header = "<div class='modal-header' style='text-align:center'>您的团购清单";
     this.header += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span style='font-size:300%;color:red'>×</span><span class=\"sr-only\">关闭</span></button>";
     
    this.content = "";
    this.head = head;
    this.footer = footer;
   
}

PageUtil.prototype.addHeader = function(strHeader)
{
	this.header += strHeader;
}

PageUtil.prototype.addContent = function(strContent)
{
	this.content += strContent;
}

PageUtil.prototype.toString = function()
{
	var content = this.head+this.header+"</div>"+this.content+this.footer;
	return content;
}