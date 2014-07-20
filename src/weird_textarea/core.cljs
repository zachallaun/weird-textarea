(ns weird-textarea.core
  (:require [om.core :as om]
            [om.dom :as dom]))

(defn textarea-example-inner [{:keys [value]} owner {:keys [on-change]}]
  (reify
    om/IRender
    (render [_]
      (dom/textarea #js {:value value
                         :onChange #(on-change (.. % -target -value))

                         ;; if the following line is present, the
                         ;; cursor will jump to the end on every
                         ;; keypress.
                         :onKeyDown #(om/set-state! owner :foo :bar)

                         ;; if the following line is present, the
                         ;; cursor will jump to the end if you type
                         ;; very quickly.
                         ;; :onKeyUp #(om/set-state! owner :foo :bar)
                         }))))

(defn textarea-example [_ owner]
  (reify
    om/IInitState
    (init-state [_]
      {:value "type anywhere\ncursor moves here ->"})

    om/IRenderState
    (render-state [_ {:keys [value]}]
      (om/build textarea-example-inner
                {:value value}
                {:opts {:on-change #(om/set-state! owner :value %)}}))))


(defn app [_ owner]
  (reify
    om/IRender
    (render [this]
      (om/build textarea-example nil))))

(om/root
  app
  nil
  {:target (. js/document (getElementById "app"))})
