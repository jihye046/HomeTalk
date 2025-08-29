class UploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }

    upload() {
        return this.loader.file
            .then(file => this._convertToWebP(file))  // WebP 변환
            .then(webpBlob => new Promise((resolve, reject) => {
                this._initRequest();
                this._initListeners(resolve, reject);
                this._sendRequest(webpBlob);
            }));
    }

    _convertToWebP(file) {
        return new Promise((resolve, reject) => {
            const img = new Image();
            const reader = new FileReader();

            reader.onload = e => {
                img.src = e.target.result;
            };
            reader.onerror = reject;
            reader.readAsDataURL(file);

            img.onload = () => {
                const canvas = document.createElement('canvas');
                canvas.width = img.width;
                canvas.height = img.height;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0);

                canvas.toBlob(blob => {
                    if(blob) {
                        resolve(blob);
                    } else {
                        reject(new Error("WebP 변환 실패"));
                    }
                }, 'image/webp', 0.8); // 품질 0.8
            };

            img.onerror = reject;
        });
    }

    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        const initRequestUrl = document.querySelector("#initRequestUrl").getAttribute("data-initRequestUrl");
        xhr.open('POST', initRequestUrl, true);
        xhr.responseType = 'json';
    }

    _initListeners(resolve, reject) {
        const xhr = this.xhr;
        const genericErrorText = '파일을 업로드 할 수 없습니다.';

        xhr.addEventListener('error', () => reject(genericErrorText));
        xhr.addEventListener('abort', () => reject());
        xhr.addEventListener('load', () => {
            const response = xhr.response;
            if(!response || response.error) {
                return reject(response && response.error ? response.error.message : genericErrorText);
            }
            resolve({
                default: response.url // 업로드된 파일 URL
            });
        });
    }

    _sendRequest(fileBlob) {
        const data = new FormData();
        data.append('upload', fileBlob, 'image.webp');
        this.xhr.send(data);
    }
}