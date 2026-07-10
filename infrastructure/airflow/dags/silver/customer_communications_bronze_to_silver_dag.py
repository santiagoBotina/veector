from dags.silver.factory import make_silver_dag

make_silver_dag(
    dag_id="customer_communications_bronze_to_silver",
    spark_class="com.veector.bronze_to_silver.customercommunications.Job",
    schedule="@daily",
    tags=["transformation", "customer_communications", "silver"],
)()
