from dags.silver.factory import make_silver_dag

make_silver_dag(
    dag_id="crm_bronze_to_silver",
    spark_class="com.veector.bronze_to_silver.crm.Job",
    schedule="@daily",
    tags=["transformation", "crm", "silver"],
)()
