

(function(window) {
    // 如果已经初始化过，直接返回避免重复声明
    if (window.Toast) return;

    window.Toast = {
        success: (msg) => {
            Swal.fire({ icon: 'success', title: msg, toast: true, position: 'top-end', showConfirmButton: false, timer: 2000, width: '220px', padding: '0.5rem' });
        },
        error: (msg) => {
            Swal.fire({ icon: 'error', title: '操作失败', text: msg || '未知错误', confirmButtonColor: '#00aaff', width: '320px' });
        },
        warning: (msg) => {
            Swal.fire({ icon: 'warning', title: '提醒', text: msg, toast: true, position: 'top-end', showConfirmButton: false, timer: 2000 });
        },
        confirm: (title, text, callback) => {
            Swal.fire({
                title: title,
                text: text,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#00aaff',
                cancelButtonColor: '#6c757d',
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then((result) => {
                if (result.isConfirmed && typeof callback === 'function') { callback(); }
            });
        },
        prompt: (title, placeholder, callback) => {
            const promptTitle = title || '请输入信息';
            const promptPlaceholder = placeholder || '请输入...';
            Swal.fire({
                title: promptTitle,
                input: 'text',
                inputPlaceholder: promptPlaceholder,
                showCancelButton: true,
                confirmButtonColor: '#00aaff',
                confirmButtonText: '提交',
                cancelButtonText: '取消',
                inputValidator: (value) => { if (!value) return '内容不能为空！'; }
            }).then((result) => {
                if (result.isConfirmed && typeof callback === 'function') { callback(result.value); }
            });
        }
    };
})(window);