from datetime import datetime

from airflow.decorators import dag
from airflow.operators.bash import BashOperator


@dag(
    dag_id="crm_bronze_to_silver",
    start_date=datetime(2024, 1, 1),
    schedule="@daily",
    catchup=False,
    tags=["transformation", "crm", "silver"],
)
def pipeline():

    BashOperator(
        task_id="scala_job_run",
        bash_command="""
        spark-submit \
          --master spark://spark-master:7077 \
          --class com.veector.bronze_to_silver.crm.Job \
          /opt/airflow/project/transformations/target/out/jvm/scala-2.13.16/transformations/veector-transformations.jar
        """
    )


pipeline()