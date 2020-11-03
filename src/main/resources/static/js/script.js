function isNumberKey(evt) {
	var charCode = (evt.wich) ? evt.wich : evt.keyCode;
	
	if ((charCode >= 48 && charCode <= 57) || charCode <= 31) {
		return true;
	}
	
	return false;
}

function isNumberKeyCurrency(evt) {
  evt = (evt) ? evt : (window.event) ? event : null;
  if (evt) {
    var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : 0));
    
    if (charCode == 8 || charCode == 44 || charCode == 46 || (charCode >= 48 && charCode <= 57)) 
        return true;
  }
  
  return false;
}

function searchRest(categoryId) {
	
	var t = document.getElementById("searchType");
	
	if (categoryId == null) {
		t.value = "TEXT";
	
	} else {
		t.value = "CATEGORY";
		document.getElementById("categoryId").value = categoryId;
	}
	
	document.getElementById("form").submit();
}

function setCmd(cmd) {
	document.getElementById("cmd").value = cmd;
	document.getElementById("form").submit();
}

function filterMenu(category) {
	document.getElementById("category").value = category;
	document.getElementById("filterForm").submit();
}

function filterMenu2() {
	document.getElementById("filterForm").submit();
}