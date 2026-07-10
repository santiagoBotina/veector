FROM apache/airflow:2.8.1

COPY requirements.txt /requirements.txt

COPY /sources /sources

RUN pip install --no-cache-dir -r /requirements.txt

USER root
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    ca-certificates \
    openjdk-17-jre-headless \
    && rm -rf /var/lib/apt/lists/*

ENV SPARK_VERSION=3.5.3 \
    SPARK_HOME=/opt/spark

RUN wget --progress=dot:giga https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.tgz \
    && tar -xzf spark-${SPARK_VERSION}-bin-hadoop3.tgz -C /opt/ \
    && mv /opt/spark-${SPARK_VERSION}-bin-hadoop3 ${SPARK_HOME} \
    && rm spark-${SPARK_VERSION}-bin-hadoop3.tgz

ENV PATH=$PATH:${SPARK_HOME}/bin:${SPARK_HOME}/sbin

USER airflow
