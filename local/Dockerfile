FROM docker.elastic.co/infra/jenkins:202102102240.c37e3f69e293

COPY configs/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
