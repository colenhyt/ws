

PageUtil = function(id,zIndex,contentClass){
	this.id = id;
	var head = ""; 
	head += "<div class='modal fade' id='"+id+"'";
	if (zIndex>0)
		head += " style='z-index:"+zIndex+"'>";
	else
		head += ">"
	head += "<div class='modal-dialog' id='"+id+"_dialog'>";		
   	head += "<div class='modal-content'>";
    
    var footer = "";
	footer += "</div>";
	footer += "</div>";
    footer +="</div>";
    
     this.header = "<div class='modal-header'>";
     
    this.content = "";
    this.head = head;
    this.footer = footer;
   
}

PageUtil.prototype.buildSingleTab = function()
{
	var header = "<ul id='"+this.id+"Tab' class='nav nav-tabs'>"
    header += "</ul>"	
	this.addHeader(header);
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