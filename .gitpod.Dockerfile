FROM gitpod/workspace-full

# Use /home/gitpod as home directory for the right user group.
# Install Confluent Tar ball that support Linux distribution
# Download Apache Kafka Head quarters library GUI to manage Kafka
# 
#
# More information: https://www.gitpod.io/docs/config-docker/
ENV HOME /home/gitpod/lib
ENV CONFLUENT_HOME ${HOME}/confluent-5.5.1
ENV WORKSPACE /workspace/orders-view
ENV PATH ${CONFLUENT_HOME}/bin:$PATH
ENV PATH /home/gitpod/bin:${PATH}

RUN mkdir -p "${BINARY_DIR}"  
RUN sudo curl -L -s http://packages.confluent.io/archive/5.5/confluent-community-5.5.1-2.12.tar.gz | tar xvz -C ${BINARY_DIR}
RUN curl -L --http1.1 https://cnfl.io/cli | sudo sh -s -- v1.6.0 -b /usr/local/bin
RUN curl -LO https://github.com/tchiotludo/akhq/releases/download/0.15.0/akhq.jar && \
  mv akhq.jar $BINARY_DIR
