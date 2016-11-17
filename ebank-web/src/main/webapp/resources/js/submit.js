/**
 * Created by liwenhe on 2016/11/1.
 */
var currentWindow = undefined;
// 表单提交时操作
layui.use('form', function(){
    var form = layui.form();
    form.on('submit', function () {
        var _this = this;
        var index = parent.layer.load();
        $.ajax({
            url:  this.action,
            type: this.method,
            data: $(this).serialize(),
            success: function (result) {
                parent.layer.close(index);
                if (result.type === "SUCCESS") {
                    var currentWinIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.alert(result.message, function (alertIndex) {
                        parent.layer.close(alertIndex);
                        parent.layer.close(currentWinIndex);
                        currentWindow.location.reload();
                    });
                } else {
                    if (result.data !== null && result.data !== undefined) {
                        $.each(result.data, function () {
                            var elem = $(_this).find("*[name=" + this.field + "]");
                            layer.tips(this.defaultMessage, elem, {tips: [3, '#c00']})
                            return false;
                        });
                    } else {
                        parent.layer.alert(result.message);
                    }
                }
            }
        });
        return false;
    });

    window.submit = function (_pWindow) {
        currentWindow = _pWindow;
        $(".layui-form").submit();
    }
});