function getUserMoviceList() {
    alert(3);
    $.ajax({
        url: "getUserMoviceList.do?userId=1",
        type: "POST",
        async: false,
        cache: true,
        success: function (dataJson) {
            alert(dataJson);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}