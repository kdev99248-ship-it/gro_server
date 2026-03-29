#!/bin/bash
# Đợi 60 giây
#sleep 60

# Tắt chương trình Java (nếu đang chạy)
pkill -f "java -jar out/artifacts/Nro_jar/Nro.jar"

# Khởi động lại chương trình và hiển thị log trực tiếp
java -jar out/artifacts/Nro_jar/Nro.jar