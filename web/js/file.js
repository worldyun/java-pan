
var app = new Vue({
    el: '#files',
    data: {
        dirlist: [],
        fileslist: [],
        nodetree: [-1],
        newdirname: "",
        now: { type: 'dir', id: -1 },
        num: true,
        uploading: false
    },
    methods: {
        getlist: function (did) {
            app.dirlist = [];
            app.fileslist = [];
            app.nodetree.push(did);
            let url = "./getlist?did=" + did;
            axios.get(url)
                .then(function (response) {
                    if (response.data.getlist) {
                        app.dirlist = response.data.data.dirlist;
                        app.fileslist = response.data.data.fileslist;
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        logout: function (e) {
            e.preventDefault();
            axios.post('./user', {
                type: 'logout'
            })
                .then(function (response) {

                    setTimeout(function () {
                        window.location.href = "./index.html";
                    }, 200);

                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        modal: function (e) {
            $('#myModal').modal('show');
        },
        newdir: function (e) {
            if (this.newdirname.length == 0) {
                return;
            }
            let url = "./newdir?pnode=" + this.nodetree[this.nodetree.length - 1] + "&dname=" + this.newdirname;
            axios.get(url)
                .then(function (response) {
                    if (response.data.newdir) {
                        app.getlist(app.nodetree.pop());
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
            this.newdirname = "";
        },
        more: function (type, did) {
            this.now.type = type;
            this.now.id = did;
            switch (type) {
                case "dir":
                    $('#dirmore').modal('show');
                    break;
                case "file":
                    $('#filemore').modal('show');
                    break;
                default:
                    break;
            }

        },
        del: function (e) {
            console.log(this.now)
            let url = "./del?type=" + this.now.type + "&id=" + this.now.id;
            axios.get(url)
                .then(function (response) {
                    if (response.data.del) {
                        app.getlist(app.nodetree.pop());
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
            this.now.type = "";
            this.now.id = -1;
        },
        fileload: function (e) {
            
            this.$refs.refFile.dispatchEvent(new MouseEvent('click'));
        },
        upload: function (e) {
            this.uploading = true;
            const selectedFile = this.$refs.refFile.files[0];
            md5(selectedFile);
        },
        uplodmd5: function (file, md5) {
            // console.log(file.name + "zzzz"+ md5)
            let formData = new FormData();
            formData.append('file', file);
            let url = './upload?did=' + this.nodetree[this.nodetree.length - 1] + "&md5=" + md5;
            axios.post(url, formData)
                .then(function (response) {
                    if (response.data.upload) {
                        app.getlist(app.nodetree.pop());
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
            this.num = false;
            setTimeout(function () {
                app.num = true;
                app.uploading = false;
            }, 100)
            
        },
        name: function (file) {
            if (file.type) {
                return file.fname + '.' + file.type;
            }else{
                return file.fname
            }
        },
        download: function () {
            var elemIF = document.createElement('iframe');
            let url = "./download?fid=" + this.now.id;
            elemIF.src = url;
            elemIF.style.display = 'none';
            document.body.appendChild(elemIF);
        }
    },
    computed: {
        subtree: function () {
            if (this.nodetree.length == 1) {
                return -1;
            } else {
                this.nodetree.pop();
                return this.nodetree.pop();
            }
        },
        last: function () {
            if (this.nodetree[this.nodetree.length - 1] == -1) {
                return true;
            } else {
                return false;
            }
        },
        md5: function (file) {

        }
        
    }
})

$(window).load(function () {
    app.getlist(-1);
});